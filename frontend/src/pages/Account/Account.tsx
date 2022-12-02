import { useState } from "react";
import { Navigate } from "react-router-dom";
import { useAppSelector } from "../../store/hook";
import style from "./Account.module.css";
import AlertSuccess from "../../components/AlertSuccess";
import { ImProfile } from "react-icons/im";
import { RiEdit2Fill } from "react-icons/ri";
import MyProfile from "./MyProfile";
import ChangePassword from "./ChangePassword";

const Account = () => {
  const isLoggedIn = useAppSelector((state) => state.auth.isLoggedIn);
  const [isSending, setIsSending] = useState<boolean>(false);
  const [navActive, setNavActive] = useState<boolean>(true);

  if (!isLoggedIn) return <Navigate to="/login" />;

  return (
    <div id={`${style.accountPage}`}>
      {isSending ? (
        <AlertSuccess
          setIsSending={() => setIsSending(false)}
          content="Profile updated"
        />
      ) : (
        <></>
      )}
      <div id={`${style.containerNo1}`}>
        <div id={`${style.containerNo2}`}>
          <div id={`${style.containerNo3}`}>
            <br />
            <h4>BookaBook</h4>
            <h4>account</h4>
            <br />
            <div
              className={
                navActive ? `${style.titleActive}` : `${style.titleNormal}`
              }
              onClick={() => setNavActive(true)}
            >
              <ImProfile color={navActive ? "#fff" : "#666"} fontSize={24} />
              <span>My profile</span>
            </div>
            <br />
            <div
              className={
                navActive ? `${style.titleNormal}` : `${style.titleActive}`
              }
              onClick={() => setNavActive(false)}
            >
              <RiEdit2Fill color={navActive ? "#666" : "#fff"} fontSize={24} />
              <span>Change password</span>
            </div>
          </div>
          <div
            id={`${style.containerNo4}`}
            style={navActive ? { display: "block" } : { display: "none" }}
          >
            <h4>My profile</h4>
            <hr
              style={{ height: "3px", borderWidth: 0, backgroundColor: "#666" }}
            />
            {/* <div style={{ display: "flex", justifyContent: "space-between" }}>
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
                      <Form.Text className="text-danger">
                        {error.firstName}
                      </Form.Text>
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
                      <Form.Text className="text-danger">
                        {error.lastName}
                      </Form.Text>
                    ) : null}
                  </Form.Group>

                  <Form.Group className="mb-3" controlId="email">
                    <div className={`${style.formField}`}>
                      <Form.Label>Email address</Form.Label>
                      <p className="m-0" style={{width: "200px", textAlign: "start"}}>{info.email}</p>
                    </div>
                    {error?.email ? (
                      <Form.Text className="text-danger">
                        {error.email}
                      </Form.Text>
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
                      <Form.Text className="text-danger">
                        {error.phoneNumber}
                      </Form.Text>
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
                              checked={gen.toLowerCase() === info.gender.toLowerCase() ? true : false}
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
                      <Form.Text className="text-danger">
                        {error.gender}
                      </Form.Text>
                    ) : null}
                  </Form.Group>
                  {errMessage ? <p className="text-danger">{errMessage}</p> : null}
            <Button variant="contained" size="small" type="submit">Save</Button>
                </Form>
              </div>
              <div style={{ textAlign: "center" }}>
                <img
                  src={info.avatar}
                  alt={info.firstName}
                  style={{ maxWidth: "100px", borderRadius: "50%" }}
                />
                <br />
                <br />
                <input
                  accept=".jpg,.jpeg,.png"
                  style={{ display: "none" }}
                  id="outlined-button-file"
                  type="file"
                />
                <label htmlFor="outlined-button-file">
                  <Button variant="outlined" component="span" size="small">
                    Upload photo
                  </Button>
                </label>
              </div>
            </div> */}
            <MyProfile setIsSending={() => setIsSending(true)} />
          </div>
          <div
            id={`${style.containerNo4}`}
            style={!navActive ? { display: "block" } : { display: "none" }}
          >
            <h4>Change password</h4>
            <hr
              style={{ height: "3px", borderWidth: 0, backgroundColor: "#666" }}
            />
            {/* <Form className={`${style.form}`} onSubmit={onChangePassSave}>
            <Form.Group className="mb-3" controlId="password">
              <div className={`${style.formField}`} style={{justifyContent: "flex-start"}}>
                <Form.Label style={{width: "35%"}}>Current password</Form.Label>
            
                <div className={`${style.indexBtn}`}>
                <Form.Control
                  className={`${style.formInput}`}
                  type={oldPassType.type}
                  value={pass.oldPassword}
                  onChange={(e: React.ChangeEvent) =>
                    setPass({
                      ...pass,
                      oldPassword: (e.target as HTMLInputElement).value.replaceAll(' ', ''),
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
              <div className={`${style.formField}`}  style={{justifyContent: "flex-start"}}>
                <Form.Label style={{width: "35%"}}>New Password</Form.Label>
                <div className={`${style.indexBtn}`}>
                <Form.Control
                  className={`${style.formInput}`}
                  type={newPassType.type}
                  value={pass.newPassword}
                  onChange={(e: React.ChangeEvent) =>
                    setPass({
                      ...pass,
                      newPassword: (e.target as HTMLInputElement).value.replaceAll(' ', ''),
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
                    <BiHide className="me-2"/>
                  ) : (
                    <AiOutlineEye className="me-2" />
                  )}
                  {newPassType.text}
                </button></div>
              </div>
              {errPass?.newPassword ? (
                <Form.Text className="text-danger">{errPass.newPassword}</Form.Text>
              ) : null}
            </Form.Group>

            <Form.Group className="mb-3" controlId="confirmPassword">
              <div className={`${style.formField}`}  style={{justifyContent: "flex-start"}}>
                <Form.Label style={{width: "35%"}}>Confirm password</Form.Label>
                <div className={`${style.indexBtn}`}>
                <Form.Control
                  className={`${style.formInput}`}
                  type={confirmPassType.type}
                  value={pass.cfPassword}
                  onChange={(e: React.ChangeEvent) =>
                    setPass({
                      ...pass,
                      cfPassword: (e.target as HTMLInputElement).value.replaceAll(' ', ''),
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
                </button></div>
              </div>
              {errPass?.cfPassword ? (
                <Form.Text className="text-danger">
                  {errPass.cfPassword}
                </Form.Text>
              ) : null}
            </Form.Group>
            {errMessage ? <p className="text-danger">{errMessage}</p> : null}
            <Button variant="contained" size="small" type="submit">Save</Button>
            </Form> */}
            <ChangePassword setIsSending={() => setIsSending(true)} />
          </div>
        </div>
      </div>
    </div>
  );
};

export default Account;
