import React, { Suspense } from "react";
import { Routes, Route } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
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
import { createTheme, ThemeProvider } from "@material-ui/core/styles";
// import DashBoardUser from "./pages/DashBoard/User/User";
// import DashBoardBook from "./pages/DashBoard/Books/Book";

const Login = React.lazy(() => import("./pages/Login/Login"));
const SignUp = React.lazy(() => import("./pages/Signup/SignUp"));
const Home = React.lazy(() => import("./pages/Home/Home"));
const Layout = React.lazy(() => import("./components/Layout"));
const Loading = React.lazy(() => import("./pages/Loading"));
// const DashBoardLayout = React.lazy(
//   () => import("./pages/DashBoard/DashBoardLayout")
// );

const theme = createTheme({
  palette: {
    primary: {
      main: "#008B8B",
    },
    secondary: {
      main: "#3F3E3E",
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
            {/* Product Collection Account AboutUs Blog Checkout Order */}
          </Route>
          {/* <Route path="dashboard" element={<DashBoardLayout />}>
            <Route index element={<DashBoardUser />} />
            <Route path="books" element={<DashBoardBook />} />
          </Route> */}
          <Route path="login" element={<Login />} />
          <Route path="signup" element={<SignUp />} />
          <Route path="verify-email" element={<AuthVerify />} />
          <Route path="forget-password" element={<ForgetPasswordLayout />}>
            <Route index element={<Forget />} />
            <Route path="verify" element={<CodeVerify />} />
            <Route path="reset/:resetToken" element={<Reset />} />
          </Route>
          {/* <Route path='*' element={<Home />} /> */}
        </Routes>
      </Suspense>
    </ThemeProvider>
  );
};

export default App;
