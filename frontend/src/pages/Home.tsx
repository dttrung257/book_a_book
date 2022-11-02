import React from "react";
import { Navigate, useNavigate } from "react-router-dom";

import { authActions } from "../store/authSlice";
import { useAppDispatch, useAppSelector } from "../store/hook";

const Home = () => {
	const isLoggedIn = useAppSelector((state) => state.auth.isLoggedIn);

	const navigate = useNavigate();
	const dispatch = useAppDispatch();
	const { user, accessToken } = useAppSelector((state) => state.auth);

	const onLogout = () => {
		dispatch(authActions.logout());
	};

	if (!isLoggedIn) return <Navigate to='/login' />;

	return (
		<div>
			<h1>Home</h1>
			<div>
				<ul>
					<li>{user.firstName}</li>
					<li>{user.lastName}</li>
					<li>{JSON.stringify(user.role)}</li>
					<li>{accessToken}</li>
				</ul>
			</div>
			<button onClick={() => navigate("/test")}>click to test</button>
			<button onClick={onLogout}>Logout</button>
		</div>
	);
};

export default Home;
