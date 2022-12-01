import { useState } from "react";
import { Form } from "react-bootstrap";
import { changeUserPassword } from "../../apis/auth";
import { changePass, PassError } from "../../models";
import { useAppSelector } from "../../store/hook";
import validator from "validator";
import style from "./Account.module.css";
import { isAxiosError } from "../../apis/axiosInstance";
import { Button } from "@mui/material";
import { BiHide } from "react-icons/bi";
import { AiOutlineEye } from "react-icons/ai";
import { checkPassword } from "../../utils";

interface PassType {
  type: "text" | "password";
  text: "Hide" | "Show";
}

const Show: PassType = {
  type: "text",
  text: "Hide",
};

const Hide: PassType = {
  type: "password",
  text: "Show",
};
const PassValidator = (pass: changePass) => {
  const error: PassError = {};
  if (!pass.oldPassword) error.oldPassword = "Current password is required";
  else if (!validator.isStrongPassword(pass.oldPassword, { minNumbers: 0 }))
    error.oldPassword =
      "Current password must have at least 8 characters, 1 uppercase, 1 lowercase and 1 symbol";
  const passwordError = checkPassword(pass.newPassword, pass.cfPassword);
  if (passwordError.password) error.newPassword = passwordError.password;
  if (passwordError.confirmPassword)
    error.cfPassword = passwordError.confirmPassword;
  if (pass.oldPassword === pass.newPassword)
    error.newPassword = "New password can not be the same as current password";
  return error;
};
const ChangePassword = ({ setIsSending }: { setIsSending: () => void }) => {
  const accessToken = useAppSelector((state) => state.auth.accessToken);
  const [errPass, setErrPass] = useState<PassError>({});
  const [errMessage, setErrMessage] = useState<string>("");
  const [oldPassType, setOldPassType] = useState<PassType>(Hide);
  const [newPassType, setNewPassType] = useState<PassType>(Hide);
  const [confirmPassType, setConfirmPassType] = useState<PassType>(Hide);
  const [pass, setPass] = useState<changePass>({
    newPassword: "",
    oldPassword: "",
    cfPassword: "",
  });
  const onChangePassSave = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const err = PassValidator(pass);
    if (err && Object.keys(err).length !== 0) return setErrPass(err);
    setErrPass({});
    setPass({
      newPassword: "",
      oldPassword: "",
      cfPassword: "",
    });
    setConfirmPassType(Hide);
    setNewPassType(Hide);
    setOldPassType(Hide);
    changeUserPassword(pass, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    })
      .then((res) => {
        console.log(res);
        setIsSending();
      })
      .catch((err) => {
        if (isAxiosError(err)) {
          const data = err.response?.data;
          setErrMessage(data?.message);
        } else {
          setErrMessage("Unknown error!!!");
          console.log(err);
        }
      });
    setErrMessage("");
  };
  return (
    <Form className={`${style.form}`} onSubmit={onChangePassSave}>
      <Form.Group className="mb-3" controlId="password">
        <div
          className={`${style.formField}`}
          style={{ justifyContent: "flex-start" }}
        >
          <Form.Label style={{ width: "35%" }}>Current password</Form.Label>

          <div className={`${style.indexBtn}`}>
            <Form.Control
              className={`${style.formInput}`}
              type={oldPassType.type}
              value={pass.oldPassword}
              onChange={(e: React.ChangeEvent) =>
                setPass({
                  ...pass,
                  oldPassword: (e.target as HTMLInputElement).value.replaceAll(
                    " ",
                    ""
                  ),
                })
              }
            />
            <button
              type="button"
              onClick={() =>
                setOldPassType((prev) => {
                  return prev.type === "text" ? Hide : Show;
                })
              }
              tabIndex={-1}
            >
              {oldPassType.type === "text" ? (
                <BiHide className="me-2" />
              ) : (
                <AiOutlineEye className="me-2" />
              )}
              {oldPassType.text}
            </button>
          </div>
        </div>
        {errPass?.oldPassword ? (
          <Form.Text className="text-danger">{errPass.oldPassword}</Form.Text>
        ) : null}
      </Form.Group>

      <Form.Group className="mb-3" controlId="password">
        <div
          className={`${style.formField}`}
          style={{ justifyContent: "flex-start" }}
        >
          <Form.Label style={{ width: "35%" }}>New Password</Form.Label>
          <div className={`${style.indexBtn}`}>
            <Form.Control
              className={`${style.formInput}`}
              type={newPassType.type}
              value={pass.newPassword}
              onChange={(e: React.ChangeEvent) =>
                setPass({
                  ...pass,
                  newPassword: (e.target as HTMLInputElement).value.replaceAll(
                    " ",
                    ""
                  ),
                })
              }
            />
            <button
              type="button"
              onClick={() =>
                setNewPassType((prev) => {
                  return prev.type === "text" ? Hide : Show;
                })
              }
            >
              {newPassType.type === "text" ? (
                <BiHide className="me-2" />
              ) : (
                <AiOutlineEye className="me-2" />
              )}
              {newPassType.text}
            </button>
          </div>
        </div>
        {errPass?.newPassword ? (
          <Form.Text className="text-danger">{errPass.newPassword}</Form.Text>
        ) : null}
      </Form.Group>

      <Form.Group className="mb-3" controlId="confirmPassword">
        <div
          className={`${style.formField}`}
          style={{ justifyContent: "flex-start" }}
        >
          <Form.Label style={{ width: "35%" }}>Confirm password</Form.Label>
          <div className={`${style.indexBtn}`}>
            <Form.Control
              className={`${style.formInput}`}
              type={confirmPassType.type}
              value={pass.cfPassword}
              onChange={(e: React.ChangeEvent) =>
                setPass({
                  ...pass,
                  cfPassword: (e.target as HTMLInputElement).value.replaceAll(
                    " ",
                    ""
                  ),
                })
              }
            />
            <button
              type="button"
              onClick={() =>
                setConfirmPassType((prev) => {
                  return prev.type === "text" ? Hide : Show;
                })
              }
              tabIndex={-1}
            >
              {confirmPassType.type === "text" ? (
                <BiHide className="me-2" />
              ) : (
                <AiOutlineEye className="me-2" />
              )}
              {confirmPassType.text}
            </button>
          </div>
        </div>
        {errPass?.cfPassword ? (
          <Form.Text className="text-danger">{errPass.cfPassword}</Form.Text>
        ) : null}
      </Form.Group>
      {errMessage ? <p className="text-danger">{errMessage}</p> : null}
      <Button variant="contained" size="small" type="submit">
        Save
      </Button>
    </Form>
  );
};
export default ChangePassword;
