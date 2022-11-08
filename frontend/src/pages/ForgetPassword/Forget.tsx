import React, { useState } from "react";
import { useNavigate, useOutletContext } from "react-router-dom";
import { Form, Button } from "react-bootstrap";
import { AxiosResponse } from "axios";
import { isAxiosError } from "../../apis/axiosInstance";
import Loading from "../Loading";

interface ContextType {
  sendForgetPassword: () => Promise<AxiosResponse>;
  email: string;
  setEmail: (email: string) => void;
}

const Forget = () => {
  const navigate = useNavigate();
  const [errMessage, setErrMessage] = useState<string>("");
  const [isSending, setIsSending] = useState<boolean>(false);
  const { sendForgetPassword, email, setEmail } =
    useOutletContext<ContextType>();

  const onFindAccountSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    try {
      e.preventDefault();
      setIsSending(true);

      const response = await sendForgetPassword();
      console.log(response);

      return navigate("verify");
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
      <h3>Find Your Account</h3>
      <Form onSubmit={onFindAccountSubmit} className="pt-3 border-top">
        <Form.Group className={`mb-md-4 mb-3`} controlId="email">
          <Form.Label className={``}>
            Please enter your email address to search for your account.
          </Form.Label>
          <Form.Control
            className={`py-2 px-3`}
            type={"email"}
            required
            placeholder={"Email address"}
            value={email}
            onChange={(e: React.ChangeEvent) => {
              setEmail((e.target as HTMLInputElement).value);
            }}
          />
        </Form.Group>

        {errMessage && <p className="text-danger text-start">{errMessage}</p>}

        <div className="d-flex justify-content-end flex-fill">
          <Button
            className={`ms-2`}
            variant="secondary"
            type="button"
            onClick={() => navigate("/login")}
          >
            Cancel
          </Button>
          <Button className={`ms-2`} variant="primary" type="submit">
            Search
          </Button>
        </div>
      </Form>
    </div>
  );
};

export default Forget;
