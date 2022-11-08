import React from "react";
import { AiFillThunderbolt } from "react-icons/ai";
import { Navigate, useNavigate } from "react-router-dom";
import Slide from "../../components/SlideShow";
import Span from "../../components/Span";

import { authActions } from "../../store/authSlice";
import { useAppDispatch, useAppSelector } from "../../store/hook";

const Home = () => {
  const isLoggedIn = useAppSelector((state) => state.auth.isLoggedIn);

  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const { user, accessToken } = useAppSelector((state) => state.auth);

  const onLogout = () => {
    dispatch(authActions.logout());
  };

  if (!isLoggedIn) return <Navigate to="/login" />;
  console.log(user.role);

  return (
    <div>
      <div style={{ marginBottom: "20px" }}>
        <Slide />
      </div>
      <Span
        icon={<AiFillThunderbolt color="000" />}
        text="On Sale"
        rectLeftWidth={100}
        rectRightWidth={window.screen.width}
        rectText="All"
      />
      {/* <h1>Home</h1>
			<div>
				<ul>
					<li>{user.firstName}</li>
					<li>{user.lastName}</li>
					<li>{JSON.stringify(user.role)}</li>
					<li>{accessToken}</li>
				</ul>
			</div>
			<button onClick={() => navigate("/test")}>click to test</button>
			<button onClick={onLogout}>Logout</button> */}
    </div>
  );
};

export default Home;
