import React, { useRef, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Form, Button } from "react-bootstrap";
import { BiHide } from "react-icons/bi";
import styled from "styled-components";

import style from "./Login.module.css";
import { login } from "../../actions/authActions";
import { useAppDispatch } from "../../store/hook";
import Footer from "./Footer";

const Wrapper = styled.div`
	background-color: #f8f8f8;
	position: relative;
	overflow: auto;
	min-height: 100vh;
`;

const Login = () => {
	const formRef = useRef<HTMLFormElement>(null);
	const [email, setEmail] = useState<string>("");
	const [password, setPassword] = useState<string>("");
	const [passType, setPassType] = useState<"text" | "password">("password");
	const [errMessage, setErrMessage] = useState<string>("");
	const dispatch = useAppDispatch();

	const navigate = useNavigate();

	const onFormSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
		try {
			e.preventDefault();

			await dispatch(login({ email: email.trim(), password: password.trim() }));

			return navigate("/");
		} catch (error: unknown) {
			console.log(error);
			setErrMessage("*Incorrect email address or password");
		}
	};

	return (
		<Wrapper>
			<div className={`${style.container}`}>
				<div className={`${style.left}`}>
					{/* <img className={`${style.img1}`} src='/images/bg_login_1.png' alt='img1' />
					<img className={`${style.img2}`} src='/images/bg_login_2.png' alt='img2' /> */}
					<img className={`${style.img2}`} src='/images/book.png' alt='img2' />
				</div>
				<div className={`${style.right}`}>
					<div className={`${style.block} py-4 px-md-5 px-3 rounded-4`}>
						<h1 className='text-center mb-4'>
							<i style={{ color: "var(--primary-color)", fontWeight: 500 }}>Book</i>a<i>Book</i>
						</h1>
						<h2 className='text-center mb-4'>Login</h2>
						<Form ref={formRef} onSubmit={onFormSubmit}>
							<Form.Group className={`${style.formField} mb-3`} controlId='email'>
								<Form.Label className={`${style.formLabel}`}>Email address</Form.Label>
								<Form.Control
									className={`${style.formInput}`}
									type='email'
									required
									placeholder='Enter email'
									value={email}
									onChange={(e: React.ChangeEvent) =>
										setEmail((e.target as HTMLInputElement).value.trim())
									}
								/>
							</Form.Group>

							<Form.Group className={`${style.formField} mb-3`} controlId='password'>
								<Form.Label className={`${style.formLabel}`}>Your password</Form.Label>
								<Form.Control
									className={`${style.formInput}`}
									type={passType}
									required
									placeholder='Password'
									value={password}
									onChange={(e: React.ChangeEvent) =>
										setPassword((e.target as HTMLInputElement).value.trim())
									}
								/>
								<button
									type='button'
									onClick={() =>
										setPassType((prev) => {
											return prev === "text" ? "password" : "text";
										})
									}
								>
									<BiHide className='me-2' />
									Hide
								</button>
							</Form.Group>

							{errMessage ? <p className='text-danger text-center'>{errMessage}</p> : null}

							<Button className={`${style.btn} fs-5`} variant='primary' type='submit'>
								Log in
							</Button>
						</Form>
						<div className={`${style.divider}`}></div>

						<div className='text-center'>
							<Link to='/forget-password'>Forget your password?</Link>
						</div>
					</div>

					<div className={`${style.block} rounded-4 mt-4`}>
						<div className='text-center py-4'>
							Dont't have an account? <Link to='/signup'>Sign up</Link>
						</div>
					</div>
				</div>
			</div>

			<Footer></Footer>
		</Wrapper>
	);
};

export default Login;
