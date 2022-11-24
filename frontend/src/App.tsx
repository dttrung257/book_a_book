import React, { Suspense } from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "owl.carousel/dist/assets/owl.carousel.css";
import "owl.carousel/dist/assets/owl.theme.default.css";
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
import DashBoardUser from "./pages/DashBoard/User/User";
import DashBoardUserDetail from "./pages/DashBoard/User/UserDetail";
import DashBoardBook from "./pages/DashBoard/Books/Book";
import Category from "./pages/Category/Category";
import Product from "./pages/Product/Product";
import { createTheme, ThemeProvider } from "@mui/material";

const Login = React.lazy(() => import("./pages/Login/Login"));
const SignUp = React.lazy(() => import("./pages/Signup/SignUp"));
const Home = React.lazy(() => import("./pages/Home/Home"));
const Layout = React.lazy(() => import("./components/Layout"));
const Loading = React.lazy(() => import("./pages/Loading"));
const DashBoardLayout = React.lazy(
  () => import("./pages/DashBoard/DashBoardLayout")
);

const theme = createTheme({
  palette: {
    primary: {
      main: "#008B8B",
    },
    secondary: {
      main: "#666666",
    },
  },
});

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
          authority: user.authority,
        },
      })
    );
  }

  return (
    <ThemeProvider theme={theme}>
      <Suspense fallback={<Loading isSending={true} />}>
        <Routes>
          <Route element={<Layout />}>
            <Route path="/" element={<Home />} />
            <Route path="test" element={<Test />} />
            <Route path="product/:id/:title" element={<Product />} />
            {/* Product Collection Account AboutUs Blog Checkout Order */}
          </Route>
          <Route path="dashboard" element={<DashBoardLayout />}>
            <Route index element={<Navigate to={"users"} />} />
            <Route path="users" element={<DashBoardUser />} />
            <Route path="users/:id" element={<DashBoardUserDetail />} />
            <Route path="books" element={<DashBoardBook />} />
            <Route path="orders" element={<div>Order</div>} />
          </Route>
          <Route path="login" element={<Login />} />
          <Route path="signup" element={<SignUp />} />
          <Route path="verify-email" element={<AuthVerify />} />
          <Route path="forget-password" element={<ForgetPasswordLayout />}>
            <Route index element={<Forget />} />
            <Route path="verify" element={<CodeVerify />} />
            <Route path="reset/:resetToken" element={<Reset />} />
          </Route>
          <Route path="books" element={<Category />} />
          {/* <Route path='*' element={<Home />} /> */}
        </Routes>
      </Suspense>
    </ThemeProvider>
  );
};

export default App;
