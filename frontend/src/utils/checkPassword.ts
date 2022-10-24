import validator from "validator";

export default interface PasswordError {
	password?: string;
	confirmPassword?: string;
}

export const checkPassword = (password: string, confirmPassword: string) => {
	const error: PasswordError = {};

	if (!password) error.password = "Password is required";
	else if (!validator.isStrongPassword(password, { minNumbers: 0 }))
		error.password =
			"Password must have at least 8 characters, 1 uppercase, 1 lowercase and 1 symbol";

	if (!confirmPassword) error.confirmPassword = "Confirm password is required";
	else if (password !== confirmPassword) error.confirmPassword = "Password are not matching";

	return error;
};
