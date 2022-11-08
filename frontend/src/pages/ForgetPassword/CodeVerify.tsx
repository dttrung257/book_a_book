import React, { useState } from "react";
import { Navigate, useNavigate, useOutletContext } from "react-router-dom";
import { Form, Button } from "react-bootstrap";
import { AxiosResponse } from "axios";
import VerificationInput from "react-verification-input";
import { IoReload } from "react-icons/io5";
import { isAxiosError } from "../../apis/axiosInstance";
import style from "./CodeVerify.module.css";
import Loading from "../Loading";
import { verifyEmail } from "../../apis/auth";

interface ContextType {
  sendForgetPassword: () => Promise<AxiosResponse>;
  email: string;
}

interface Style {
  container?: string;
  character?: string;
  characterInactive?: string;
  characterSelected?: string;
}

const CodeVerify = () => {
  const navigate = useNavigate();
  const { sendForgetPassword, email } = useOutletContext<ContextType>();
  const [errMessage, setErrMessage] = useState<string>("");
  const [verifyCode, setVerifyCode] = useState<string>("");
  const [isSending, setIsSending] = useState<boolean>(false);

  if (!email) return <Navigate to="/forget-password" />;

  const className: Style = {
    container: `${style.container}`,
    character: `${style.character}`,
    characterSelected: `${style.characterSelected}`,
  };

  const onVerifyCodeSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    try {
      e.preventDefault();
      setIsSending(true);
      let resetToken;
      verifyEmail({email: email, verifyCode: verifyCode})
      .then((res)=> {
        resetToken = res.data;
      })

      return navigate(`../reset/${resetToken}`);
    } catch (error) {
      console.log(error);
      if (isAxiosError(error)) {
        const data = error.response?.data;
        setErrMessage(data?.message);
      } else {
        setErrMessage("Unknow error!!!");
        console.log(error);
      }
    } finally {
      setIsSending(false);
    }
  };

  const onResendCode = async (e: React.MouseEvent<HTMLButtonElement>) => {
    try {
      e.preventDefault();
      setIsSending(true);

      const response = await sendForgetPassword();
      console.log(response);
    } catch (error) {
      if (isAxiosError(error)) {
        const data = error.response?.data;
        setErrMessage(data?.message);
      } else {
        setErrMessage("Unknow error!!!");
        console.log(error);
      }
    } finally {
      setIsSending(false);
    }
  };

  return (
    <div>
      <Loading isSending={isSending} />

      <h3>Enter the Code</h3>
      <Form onSubmit={onVerifyCodeSubmit} className="pt-3 border-top">
        <Form.Group className={`mb-md-4 mb-3`} controlId="email">
          <Form.Label className={``}>
            Enter the 7-digits verification code mailed to your email.
          </Form.Label>
          <VerificationInput
            length={7}
            autoFocus
            placeholder=" "
            validChars="0-9"
            value={verifyCode}
            classNames={className}
            onChange={(value: string) => setVerifyCode(value)}
          />
        </Form.Group>

        {errMessage && <p className="text-danger text-start">{errMessage}</p>}

        <div className="d-flex flex-wrap ">
          <button
            type="button"
            className="bg-white border-0 pe-2"
            onClick={onResendCode}
          >
            <IoReload className="mx-2" />
            Resend Code
          </button>

          <div className="d-flex justify-content-end flex-fill">
            <Button
              className={`ms-2`}
              variant="secondary"
              type="button"
              onClick={() => navigate("/forget-password")}
            >
              Cancel
            </Button>
            <Button className={`ms-2`} variant="primary" type="submit">
              Verify
            </Button>
          </div>
        </div>
      </Form>
    </div>
  );
};

export default CodeVerify;
