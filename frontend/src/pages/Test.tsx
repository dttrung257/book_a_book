import React from "react";
import { Navigate, useNavigate } from "react-router-dom";
import { useAppSelector } from "../store/hook";

const Test = () => {
	const isLoggedIn = useAppSelector((state) => state.auth.isLoggedIn);
	const navigate = useNavigate();

	if (!isLoggedIn) return <Navigate to='/login' />;

	return (
		<div>
			Test
			<button onClick={() => navigate("/")}>click to home</button>
		</div>
	);
};

export default Test;
