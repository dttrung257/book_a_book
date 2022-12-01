import { useEffect, useState } from "react";
import { Form } from "react-bootstrap";
import { getUserInfo, updateUserInfo } from "../../apis/auth";
import { AuthError, UserDetail } from "../../models";
import { useAppDispatch, useAppSelector } from "../../store/hook";
import validator from "validator";
import style from "./Account.module.css";
import { isAxiosError } from "../../apis/axiosInstance";
import { Button } from "@mui/material";
import { ChangeEvent } from "react";
import "firebase/compat/storage";
import { storage } from "../../firebase/firebase";
import { getDownloadURL, ref, uploadBytesResumable } from "firebase/storage";
import { authActions } from "../../store/authSlice";

const metadata = {
  contentType: "image/jpeg",
  accept: ".png",
};

const authValidator = (info: UserDetail): AuthError => {
  const error: AuthError = {};

  if (!info.firstName) error.firstName = "First name is required";
  else if (!validator.isAlpha(info.firstName))
    error.firstName = " First name must contains only letters";

  if (!info.lastName) error.lastName = "Last name is required";
  else if (!validator.isAlpha(info.lastName))
    error.lastName = " Last name must contains only letters";

  if (!info.gender) error.gender = "Gender is required";
  if (info.phoneNumber && !validator.isMobilePhone(info.phoneNumber))
    error.phoneNumber = "Please enter a valid phone number";

  return error;
};
const MyProfile = ({ setIsSending }: { setIsSending: () => void }) => {
  const loginInfo = useAppSelector((state) => state.auth);
  const [url, setURL] = useState(
    loginInfo.user.avatar !== undefined
      ? loginInfo.user.avatar
      : "/images/anonymous.jpg"
  );
  const [info, setInfo] = useState<UserDetail>({
    firstName: loginInfo.user.firstName,
    lastName: loginInfo.user.lastName,
    email: "",
    gender: "",
    phoneNumber: "",
    address: "",
    avatar:
      loginInfo.user.avatar !== undefined
        ? loginInfo.user.avatar
        : "/images/anonymous.jpg",
  });
  const [error, setError] = useState<AuthError>({});
  const [errMessage, setErrMessage] = useState<string>("");
  const dispatch = useAppDispatch();

  useEffect(() => {
    getUserInfo({
      headers: {
        Authorization: `Bearer ${loginInfo.accessToken}`,
      },
    }).then((res) => {
      setInfo({
        firstName: res.firstName,
        lastName: res.lastName,
        email: res.email,
        gender: res.gender,
        phoneNumber: res.phoneNumber !== null ? res.phoneNumber : "",
        address: res.address !== null ? res.address : "",
        avatar: res.avatar !== undefined ? res.avatar : "/images/anonymous.jpg",
      });
      setURL(res.avatar !== undefined ? res.avatar : "/images/anonymous.jpg");
    });
  }, [loginInfo.accessToken]);

  const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      let file = e.target.files[0];
      if (file) {
        var ext = file.type;
        if (ext !== "image/jpeg" && ext !== "image/png") {
          setError({ ...error, avatar: "Invalid file type!" });
          return;
        }
        setError({ ...error, avatar: undefined });
        const path = `/images/${file.name}`;
        const imgref = ref(storage, path);
        const uploadTask = uploadBytesResumable(imgref, file, metadata);

        uploadTask.on(
          "state_changed",
          (snapshot) => {
            // Observe state change events such as progress, pause, and resume
            // Get task progress, including the number of bytes uploaded and the total number of bytes to be uploaded
            const progress =
              (snapshot.bytesTransferred / snapshot.totalBytes) * 100;
            console.log("Upload is " + progress + "% done");
            switch (snapshot.state) {
              case "paused":
                console.log("Upload is paused");
                break;
              case "running":
                console.log("Upload is running");
                break;
            }
          },
          (error) => {
            console.log(error);
            setError({ ...error, avatar: "Upload photo failed!" });
          },
          () => {
            getDownloadURL(uploadTask.snapshot.ref).then((downloadURL) => {
              setURL(downloadURL);
            });
          }
        );
      }
    }
  };

  const onSaveUpdate = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const err = authValidator(info);
    if (err && Object.keys(err).length !== 0) return setError(err);
    setError({});
    setInfo({ ...info, avatar: url });
    console.log(info);
    updateUserInfo(
      { ...info, avatar: url },
      {
        headers: {
          Authorization: `Bearer ${loginInfo.accessToken}`,
        },
      }
    )
      .then((res) => {
        console.log(res);
        setIsSending();
        dispatch(
          authActions.storeInfo({
            ...loginInfo,
            user: {
              ...loginInfo.user,
              firstName: info.firstName,
              lastName: info.lastName,
              avatar: info.avatar,
            },
          })
        );
      })
      .catch((error) => {
        if (isAxiosError(error)) {
          const data = error.response?.data;
          setErrMessage(data?.message);
        } else {
          setErrMessage("Unknown error!!!");
          console.log(error);
        }
      });
    setErrMessage("");
  };
  return (
    <div style={{ display: "flex", justifyContent: "space-between" }}>
      <div style={{ width: "68%" }}>
        <Form className={`${style.form}`} onSubmit={onSaveUpdate}>
          <Form.Group className="mb-3" controlId="firstName">
            <div className={`${style.formField}`}>
              <Form.Label>First name</Form.Label>
              <Form.Control
                className={`${style.formInput}`}
                type="text"
                value={info.firstName}
                onChange={(e: React.ChangeEvent) =>
                  setInfo({
                    ...info,
                    firstName: (e.target as HTMLInputElement).value,
                  })
                }
              />
            </div>
            {error?.firstName ? (
              <Form.Text className="text-danger">{error.firstName}</Form.Text>
            ) : null}
          </Form.Group>

          <Form.Group className="mb-3" controlId="lastName">
            <div className={`${style.formField}`}>
              <Form.Label>Last name</Form.Label>
              <Form.Control
                className={`${style.formInput}`}
                type="text"
                value={info.lastName}
                onChange={(e: React.ChangeEvent) =>
                  setInfo({
                    ...info,
                    lastName: (e.target as HTMLInputElement).value,
                  })
                }
              />
            </div>
            {error?.lastName ? (
              <Form.Text className="text-danger">{error.lastName}</Form.Text>
            ) : null}
          </Form.Group>

          <Form.Group className="mb-3" controlId="email">
            <div className={`${style.formField}`}>
              <Form.Label>Email address</Form.Label>
              <p className="m-0" style={{ width: "200px", textAlign: "start" }}>
                {info.email}
              </p>
            </div>
            {error?.email ? (
              <Form.Text className="text-danger">{error.email}</Form.Text>
            ) : null}
          </Form.Group>
          <Form.Group className="mb-3" controlId="phoneNumber">
            <div className={`${style.formField}`}>
              <Form.Label>Phone number</Form.Label>
              <Form.Control
                className={`${style.formInput}`}
                type="tel"
                value={info.phoneNumber}
                onChange={(e: React.ChangeEvent) =>
                  setInfo({
                    ...info,
                    phoneNumber: (e.target as HTMLInputElement).value,
                  })
                }
              />
            </div>
            {error?.phoneNumber ? (
              <Form.Text className="text-danger">{error.phoneNumber}</Form.Text>
            ) : null}
          </Form.Group>
          <Form.Group className="mb-3" controlId="address">
            <div className={`${style.formField}`}>
              <Form.Label>Address</Form.Label>
              <Form.Control
                className={`${style.formInput}`}
                type="text"
                value={info.address}
                onChange={(e: React.ChangeEvent) =>
                  setInfo({
                    ...info,
                    address: (e.target as HTMLInputElement).value,
                  })
                }
              />
            </div>
          </Form.Group>
          <Form.Group className="mb-3">
            <div
              style={{
                display: "flex",
                justifyContent: "space-between",
              }}
            >
              <Form.Label>Gender</Form.Label>
              <div style={{ display: "flex" }}>
                {["Female", "Male", "Other"].map((gen) => {
                  return (
                    <Form.Check
                      key={gen}
                      className={"m-1 mt-0"}
                      type="radio"
                      label={gen}
                      name="gender"
                      value={gen}
                      id={gen}
                      checked={
                        gen.toLowerCase() === info.gender.toLowerCase()
                          ? true
                          : false
                      }
                      onChange={(e: React.ChangeEvent) =>
                        setInfo({
                          ...info,
                          gender: (e.target as HTMLInputElement).value,
                        })
                      }
                    />
                  );
                })}
              </div>
            </div>
            {error?.gender ? (
              <Form.Text className="text-danger">{error.gender}</Form.Text>
            ) : null}
          </Form.Group>
          {errMessage ? <p className="text-danger">{errMessage}</p> : null}
          <Button variant="contained" size="small" type="submit">
            Save
          </Button>
        </Form>
      </div>
      <div style={{ textAlign: "center" }}>
        <img
          src={url}
          alt={info.firstName}
          style={{ width: "100px", height: "100px", borderRadius: "50%" }}
        />
        {error?.avatar ? (
          <p className="text-danger m-2">{error.avatar}</p>
        ) : (
          <div>
            <br />
          </div>
        )}
        <input
          accept=".jpg,.jpeg,.png"
          style={{ display: "none" }}
          id="outlined-button-file"
          type="file"
          onChange={handleChange}
        />
        <label htmlFor="outlined-button-file">
          <Button variant="outlined" component="span" size="small">
            Upload photo
          </Button>
        </label>
      </div>
    </div>
  );
};

export default MyProfile;
