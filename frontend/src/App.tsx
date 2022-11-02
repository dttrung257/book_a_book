import React from "react";
import { Routes, Route } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";

import Home from "./pages/Home";
import Login from "./pages/Login/Login";
import SignUp from "./pages/Signup/SignUp";
import "./App.css";
import Test from "./pages/Test";
import { authActions } from "./store/authSlice";
import { useAppDispatch, useAppSelector } from "./store/hook";
import Cookies from "js-cookie";
import CodeVerify from "./pages/ForgetPassword/CodeVerify";
import ForgetPasswordLayout from "./pages/ForgetPassword/Layout";
import Forget from "./pages/ForgetPassword/Forget";
import Reset from "./pages/ForgetPassword/Reset";
import AuthVerify from "./pages/VerifyEmail/AuthVerify";

const App = () => {
	const dispatch = useAppDispatch();
	const isLoggedIn = useAppSelector((state) => state.auth.isLoggedIn);

	const user = JSON.parse(Cookies.get("user") || "{}");
	const token = Cookies.get("token") || "";

	if (!isLoggedIn && user && token) {
		dispatch(
			authActions.storeInfo({
				accessToken: token,
				user: {
					firstName: user.firstName,
					lastName: user.lastName,
					role: user.role,
				},
			})
		);
	}

	return (
		<Routes>
			<Route path='/' element={<Home />} />
			<Route path='test' element={<Test />} />
			<Route path='login' element={<Login />} />
			<Route path='signup' element={<SignUp />} />
			<Route path='verify-email' element={<AuthVerify />} />
			<Route path='forget-password' element={<ForgetPasswordLayout />}>
				<Route index element={<Forget />} />
				<Route path='verify' element={<CodeVerify />} />
				<Route path='reset/:resetToken' element={<Reset />} />
			</Route>
			{/* <Route path='*' element={<Home />} /> */}
		</Routes>
	);
};

export default App;
