import React, { useState } from "react";
import { Navigate, useNavigate, useOutletContext } from "react-router-dom";
import { Form, Button } from "react-bootstrap";
import { AxiosResponse } from "axios";
import VerificationInput from "react-verification-input";
import { IoReload } from "react-icons/io5";
import axios from "../../apis/axios";
import style from "./CodeVerify.module.css";

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

	if (!email) return <Navigate to='/forget-password' />;

	const className: Style = {
		container: `${style.container}`,
		character: `${style.character}`,
		characterSelected: `${style.characterSelected}`,
	};

	console.log("Code: 1234567");
	const onVerifyCodeSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
		try {
			e.preventDefault();

			//TODO: send verifyCode to server, redirect to set new password route
			if (verifyCode !== "1234567")
				setErrMessage("The verification code you entered is incorrect. Please try again!");

			const resetId = "randomHIhiHIhi";
			return navigate(`../reset/${resetId}`);
		} catch (error) {
			setErrMessage("");
		}
	};

	const onResendCode = async (e: React.MouseEvent<HTMLButtonElement>) => {
		try {
			e.preventDefault();

			const response = await sendForgetPassword();
			console.log(response);
		} catch (error) {
			setErrMessage("");
			console.log(error);
		}
	};

	return (
		<div>
			<h3>Enter the Code</h3>
			<Form onSubmit={onVerifyCodeSubmit} className='pt-3 border-top'>
				<Form.Group className={`mb-md-4 mb-3`} controlId='email'>
					<Form.Label className={``}>
						Enter the 7-digits verification code mailed to your email.
					</Form.Label>
					<VerificationInput
						length={7}
						autoFocus
						placeholder=' '
						validChars='0-9'
						value={verifyCode}
						classNames={className}
						onChange={(value: string) => setVerifyCode(value)}
					/>
				</Form.Group>

				{errMessage && <p className='text-danger text-start'>{errMessage}</p>}

				<div className='d-flex flex-wrap '>
					<button type='button' className='bg-white border-0 pe-2' onClick={onResendCode}>
						<IoReload className='mx-2' />
						Resend Code
					</button>

					<div className='d-flex justify-content-end flex-fill'>
						<Button
							className={`ms-2`}
							variant='secondary'
							type='button'
							onClick={() => navigate("/forget-password")}
						>
							Cancel
						</Button>
						<Button className={`ms-2`} variant='primary' type='submit'>
							Verify
						</Button>
					</div>
				</div>
			</Form>
		</div>
	);
};

export default CodeVerify;
