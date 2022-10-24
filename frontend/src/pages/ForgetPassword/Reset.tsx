import React, { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { Form, Button } from "react-bootstrap";
import { FaCheckCircle } from "react-icons/fa";
import PasswordError, { checkPassword } from "../../utils/checkPassword";

const Reset = () => {
	const { resetId } = useParams();
	const navigate = useNavigate();
	const [err, setErr] = useState<PasswordError>({});
	const [password, setPassword] = useState<string>("");
	const [confirmPassword, setConfirmPassword] = useState<string>("");
	const [passwordChange, setPasswordChange] = useState<boolean>(false);

	const onFormSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
		try {
			e.preventDefault();

			const passwordError = checkPassword(password, confirmPassword);
			if (passwordError && Object.keys(passwordError).length !== 0) return setErr(passwordError);

			//TODO: send new password to server

			setPasswordChange(true);
		} catch (error) {
			console.log(error);
		}
	};

	return (
		<div>
			{!passwordChange ? (
				<div>
					<h3>Enter Your New Password</h3>
					<Form onSubmit={onFormSubmit} className='pt-3 border-top'>
						<Form.Group className={`mb-md-4 mb-3`} controlId='password'>
							<Form.Label className={``}>Password:</Form.Label>
							<Form.Control
								className={`py-2 px-3`}
								type='password'
								value={password}
								onChange={(e: React.ChangeEvent) =>
									setPassword((e.target as HTMLInputElement).value.trim())
								}
							/>
							{err?.password ? <Form.Text className='text-danger'>{err.password}</Form.Text> : null}
						</Form.Group>

						<Form.Group className={`mb-md-4 mb-3`} controlId='confirmPassword'>
							<Form.Label className={`py-2 px-3`}>Confirm password:</Form.Label>
							<Form.Control
								className={``}
								type='password'
								value={confirmPassword}
								onChange={(e: React.ChangeEvent) =>
									setConfirmPassword((e.target as HTMLInputElement).value.trim())
								}
							/>
							{err?.confirmPassword ? (
								<Form.Text className='text-danger'>{err.confirmPassword}</Form.Text>
							) : null}
						</Form.Group>

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
					</Form>
				</div>
			) : (
				<div className='text-center '>
					<div className='text-success'>
						<FaCheckCircle size={60} />
						<h3>Password changed successfully!</h3>
					</div>
					<div>
						<p>Please login to your email account again</p>
						<button className='btn btn-primary px-3 py-2' onClick={() => navigate("/login")}>
							Login now
						</button>
					</div>
				</div>
			)}
		</div>
	);
};

export default Reset;
