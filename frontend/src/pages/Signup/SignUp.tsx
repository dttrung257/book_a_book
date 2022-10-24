import React, { useState, useRef, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import styled from "styled-components";
import { Form, Button } from "react-bootstrap";
import validator from "validator";
import { BiHide } from "react-icons/bi";
import AuthInfo from "../../models/authInfo.model";
import axios from "../../apis/axios";
import style from "./SignUp.module.css";
import { checkPassword } from "../../utils/checkPassword";

interface AuthError {
	firstName?: string;
	lastName?: string;
	email?: string;
	phone?: string;
	address?: string;
	password?: string;
	confirmPassword?: string;
	gender?: string;
	acceptPrivacy?: string;
}

const authValidator = (info: AuthInfo): AuthError => {
	const error: AuthError = {};

	if (!info.firstName) error.firstName = "First name is required";
	else if (!validator.isAlpha(info.firstName))
		error.firstName = " First name must contains only letters";

	if (!info.lastName) error.lastName = "Last name is required";
	else if (!validator.isAlpha(info.lastName))
		error.lastName = " Last name must contains only letters";

	if (!info.email) error.email = "Email address is required";
	else if (!validator.isEmail(info.email)) error.email = "Please enter a valid email";

	if (!info.phone) error.phone = "Phone number is required";
	else if (!validator.isMobilePhone(info.phone)) error.phone = "Please enter a valid phone number";

	if (!info.address) error.address = "Address is required";

	const passwordError = checkPassword(info.password, info.confirmPassword);
	if (passwordError.password) error.password = passwordError.password;
	if (passwordError.confirmPassword) error.confirmPassword = passwordError.confirmPassword;

	if (!info.gender) error.gender = "Gender is required";

	if (!info.acceptPrivacy)
		error.acceptPrivacy = "Please accept to our Terms of use and Privacy Policy";

	return error;
};

const Wrapper = styled.div`
	position: fixed;
	top: 0;
	bottom: 0;
	left: 0;
	right: 0;
	background-image: url("/images/bg.jpg");
	background-repeat: no-repeat;
	background-size: cover;
	overflow: auto;
`;

const Login = () => {
	const [info, setInfo] = useState<AuthInfo>({
		firstName: "",
		lastName: "",
		email: "",
		phone: "",
		address: "",
		password: "",
		confirmPassword: "",
		gender: "",
		acceptPrivacy: false,
	});
	const [error, setError] = useState<AuthError>({});
	const [passType, setPassType] = useState<"text" | "password">("password");
	const [confirmPassType, setConfirmPassType] = useState<"text" | "password">("password");
	const firstInput = useRef<HTMLInputElement>(null);
	const navigate = useNavigate();

	useEffect(() => {
		firstInput.current?.focus();

		return () => {};
	}, []);

	const onFormSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
		try {
			e.preventDefault();

			const err = authValidator(info);
			if (err && Object.keys(err).length !== 0) return setError(err);

			//TODO: change req body, handle error
			const response = await axios.post(
				"https://identitytoolkit.googleapis.com/v1/accounts:signUp",
				{
					email: info.email,
					password: info.password,
					returnSecureToken: true,
				},
				{
					headers: {
						"Content-Type": "application/json",
					},
					params: {
						key: "AIzaSyDvI7V7DImXkf2rol7UJLJOQU7wq_4i-qQ",
					},
				}
			);

			console.log(response);
			return navigate("/login");
		} catch (error) {
			console.log(error);
		}
	};

	return (
		<Wrapper>
			<div className={`${style.container}`}>
				<div className='bg-white rounded-4 d-flex flex-column p-md-5 p-4'>
					<h1 className='text-center mb-4'>
						<i style={{ color: "var(--primary-color)", fontWeight: 500 }}>Book</i>a<i>Book</i>
					</h1>
					<h3 className='text-center mb-4'>Sign up now</h3>
					<Form className={`${style.form} px-lg-5`} onSubmit={onFormSubmit}>
						<Form.Group className='mb-3' controlId='firstName'>
							<div className={`${style.formField}`}>
								<Form.Label>First name</Form.Label>
								<Form.Control
									className={`${style.formInput}`}
									type='text'
									ref={firstInput}
									value={info.firstName}
									onChange={(e: React.ChangeEvent) =>
										setInfo({ ...info, firstName: (e.target as HTMLInputElement).value })
									}
								/>
							</div>
							{error?.firstName ? (
								<Form.Text className='text-danger'>{error.firstName}</Form.Text>
							) : null}
						</Form.Group>

						<Form.Group className='mb-3' controlId='lastName'>
							<div className={`${style.formField}`}>
								<Form.Label>Last name</Form.Label>
								<Form.Control
									className={`${style.formInput}`}
									type='text'
									value={info.lastName}
									onChange={(e: React.ChangeEvent) =>
										setInfo({ ...info, lastName: (e.target as HTMLInputElement).value })
									}
								/>
							</div>
							{error?.lastName ? (
								<Form.Text className='text-danger'>{error.lastName}</Form.Text>
							) : null}
						</Form.Group>

						<Form.Group className='mb-3' controlId='email'>
							<div className={`${style.formField}`}>
								<Form.Label>Email address</Form.Label>
								<Form.Control
									className={`${style.formInput}`}
									type='text'
									value={info.email}
									onChange={(e: React.ChangeEvent) =>
										setInfo({ ...info, email: (e.target as HTMLInputElement).value })
									}
								/>
							</div>
							{error?.email ? <Form.Text className='text-danger'>{error.email}</Form.Text> : null}
						</Form.Group>

						<Form.Group className='mb-3' controlId='phone'>
							<div className={`${style.formField}`}>
								<Form.Label>Phone number</Form.Label>
								<Form.Control
									className={`${style.formInput}`}
									type='text'
									value={info.phone}
									onChange={(e: React.ChangeEvent) =>
										setInfo({ ...info, phone: (e.target as HTMLInputElement).value })
									}
								/>
							</div>
							{error?.phone ? <Form.Text className='text-danger'>{error.phone}</Form.Text> : null}
						</Form.Group>

						<Form.Group className='mb-3' controlId='address'>
							<div className={`${style.formField}`}>
								<Form.Label>Address</Form.Label>
								<Form.Control
									className={`${style.formInput}`}
									type='text'
									value={info.address}
									onChange={(e: React.ChangeEvent) =>
										setInfo({ ...info, address: (e.target as HTMLInputElement).value })
									}
								/>
							</div>
							{error?.address ? (
								<Form.Text className='text-danger'>{error.address}</Form.Text>
							) : null}
						</Form.Group>

						<Form.Group className='mb-3' controlId='password'>
							<div className={`${style.formField}`}>
								<Form.Label>Password</Form.Label>
								<Form.Control
									className={`${style.formInput}`}
									type={passType}
									value={info.password}
									onChange={(e: React.ChangeEvent) =>
										setInfo({ ...info, password: (e.target as HTMLInputElement).value })
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
							</div>
							{error?.password ? (
								<Form.Text className='text-danger'>{error.password}</Form.Text>
							) : null}
						</Form.Group>

						<Form.Group className='mb-3' controlId='confirmPassword'>
							<div className={`${style.formField}`}>
								<Form.Label>Confirm password</Form.Label>
								<Form.Control
									className={`${style.formInput}`}
									type={confirmPassType}
									value={info.confirmPassword}
									onChange={(e: React.ChangeEvent) =>
										setInfo({ ...info, confirmPassword: (e.target as HTMLInputElement).value })
									}
								/>
								<button
									type='button'
									onClick={() =>
										setConfirmPassType((prev) => {
											return prev === "text" ? "password" : "text";
										})
									}
								>
									<BiHide className='me-2' />
									Hide
								</button>
							</div>
							{error?.confirmPassword ? (
								<Form.Text className='text-danger'>{error.confirmPassword}</Form.Text>
							) : null}
						</Form.Group>

						<Form.Group className='mb-3'>
							<Form.Label>What's your gender</Form.Label>
							{["Female", "Male", "Non-binary"].map((gen) => {
								return (
									<Form.Check
										key={gen}
										className={"m-3 mt-0"}
										type='radio'
										label={gen}
										name='gender'
										value={gen}
										id={gen}
										onChange={(e: React.ChangeEvent) =>
											setInfo({ ...info, gender: (e.target as HTMLInputElement).value })
										}
									/>
								);
							})}
							{error?.gender ? <Form.Text className='text-danger'>{error.gender}</Form.Text> : null}
						</Form.Group>

						<Form.Group className='mb-3' controlId='acceptPrivacy'>
							<div className='d-flex align-items-center'>
								<Form.Check
									className='me-3'
									type='checkbox'
									onChange={() => setInfo({ ...info, acceptPrivacy: !info.acceptPrivacy })}
									checked={info.acceptPrivacy}
								/>
								<Form.Label className='m-0'>
									By creating an account, I agree to our Terms of use and Privacy Policy.
								</Form.Label>
							</div>
							{error?.acceptPrivacy ? (
								<Form.Text className='text-danger'>{error.acceptPrivacy}</Form.Text>
							) : null}
						</Form.Group>

						<div className='d-flex align-items-center flex-wrap'>
							<Button className={`${style.btn} me-5`} variant='primary' type='submit'>
								Submit
							</Button>
							<div>
								Already have an account? <Link to={"/login"}>Log in</Link>
							</div>
						</div>
					</Form>
				</div>
			</div>
		</Wrapper>
	);
};

export default Login;
