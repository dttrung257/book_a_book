import React, { useState, useEffect, useCallback, useRef } from "react";
import { Navigate, useNavigate } from "react-router-dom";
import { Form, Button } from "react-bootstrap";
import VerificationInput from "react-verification-input";
import { FaCheckCircle } from "react-icons/fa";
import styled from "styled-components";
import { BiSend } from "react-icons/bi";
import axios, { isAxiosError } from "../../apis/axios";
import style from "./AuthVerify.module.css";
import { useAppDispatch, useAppSelector } from "../../store/hook";
import { emailVerifyActions } from "../../store/emailVerifySlice";
import Loading from "../../components/Loading";

const Container = styled.div`
	width: 600px;
	margin: 200px auto;
	border: 1px solid #ddd;
	border-radius: 12px;
	background-color: white;

	@media screen and (max-width: 767px) {
		width: 460px;
		margin-top: 160px;
	}

	@media screen and (max-width: 479px) {
		width: 100%;
		margin-top: 100px;
	}
`;

interface Style {
	container?: string;
	character?: string;
	characterInactive?: string;
	characterSelected?: string;
}

const AuthVerify = () => {
	const navigate = useNavigate();
	const dispatch = useAppDispatch();
	const effectRun = useRef<boolean>(false);
	const isLoggedIn = useAppSelector((state) => state.auth.isLoggedIn);
	const [errMessage, setErrMessage] = useState<string>("");
	const [verifyCode, setVerifyCode] = useState<string>("");
	const [verified, setVerified] = useState<boolean>(false);
	const [isSending, setIsSending] = useState<boolean>(false);
	const email = useAppSelector((state) => state.emailVerify.email);

	const sendCode = useCallback(async () => {
		console.log("code send");
		try {
			setIsSending(true);

			await axios.get(`/authen/send_email/${email}`);

			setErrMessage("");
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
	}, [email]);

	useEffect(() => {
		if (email && !effectRun.current) {
			sendCode();
			console.log(email, effectRun.current);
		}
		return () => {
			effectRun.current = true;
		};
	}, [email, sendCode]);

	if (isLoggedIn || !email) return <Navigate to='/' />;

	const className: Style = {
		container: `${style.container}`,
		character: `${style.character}`,
		characterSelected: `${style.characterSelected}`,
	};

	const onVerifyCodeSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
		try {
			e.preventDefault();
			setIsSending(true);

			const response = await axios.get(`/authen/${email}/confirm_verification/${verifyCode}`);

			console.log(response);

			setVerified(true);
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

	const onSendCode = async (e: React.MouseEvent<HTMLButtonElement>) => {
		e.preventDefault();
		sendCode();
	};

	return (
		<Container className='p-3'>
			<Loading isSending={isSending} />

			{!verified ? (
				<div>
					<h3>Activate your account</h3>
					<Form onSubmit={onVerifyCodeSubmit} className='pt-3 border-top'>
						<Form.Group className={`mb-md-4 mb-3`} controlId='email'>
							<Form.Label className={``}>
								Enter the 7-digits verification code mailed to {email}.
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
							<button type='button' className='bg-white border-0 pe-2' onClick={onSendCode}>
								<BiSend className='mx-2' />
								Send Code
							</button>

							<div className='d-flex justify-content-end flex-fill'>
								<Button
									className={`ms-2`}
									variant='secondary'
									type='button'
									onClick={() => navigate("/")}
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
			) : (
				<div className='text-center '>
					<div className='text-success'>
						<FaCheckCircle size={60} />
						<h3>Your email address has been verified</h3>
					</div>
					<div>
						<p>Thank for verifying your email on BookABook. You can now proceed to login.</p>
						<button
							className='btn btn-primary px-3 py-2'
							onClick={() => {
								navigate("/login");
								dispatch(emailVerifyActions.clearEmail());
							}}
						>
							Login now
						</button>
					</div>
				</div>
			)}
		</Container>
	);
};

export default AuthVerify;
