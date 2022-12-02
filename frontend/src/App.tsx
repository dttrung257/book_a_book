import React, { Suspense } from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import { ToastContainer, Slide } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "bootstrap/dist/css/bootstrap.min.css";
import "owl.carousel/dist/assets/owl.carousel.css";
import "owl.carousel/dist/assets/owl.theme.default.css";
import "./App.css";
import { authActions } from "./store/authSlice";
import { cartActions } from "./store/cartSlice";
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
import DashBoardOrder from "./pages/DashBoard/Orders/Order";
import DashBoardOrderDetail from "./pages/DashBoard/Orders/OrderDetail";
import CategoryPage from "./pages/Category/Category";
import Product from "./pages/Product/Product";
import AboutUs from "./pages/AboutUs/AboutUs";
import { createTheme, ThemeProvider } from "@mui/material";
import PersonalOrder from "./pages/PersonalOrder/PersonalOrder";
import Home from "./pages/Home/Home";
import Login from "./pages/Login/Login";
import Layout from "./components/Layout";
import DashBoardLayout from "./pages/DashBoard/DashBoardLayout";
import Loading from "./pages/Loading";
import SignUp from "./pages/Signup/SignUp";
import Account from "./pages/Account/Account";
const Cart = React.lazy(() => import("./pages/Cart/Cart"));

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
          avatar: user.avatar,
        },
      })
    );

    const cart = JSON.parse(localStorage.getItem("cartItems") || "[]");
    dispatch(cartActions.storeInfo(cart));
  }

  return (
    <ThemeProvider theme={theme}>
      <Suspense fallback={<Loading isSending={true} />}>
        <Routes>
          <Route element={<Layout />}>
            <Route path="/" element={<Home />} />
            <Route path="cart" element={<Cart />} />
            <Route path="product/:id/:title" element={<Product />} />
            <Route path="books" element={<CategoryPage />} />
            {/* Product Collection Account AboutUs Blog Checkout Order */}
          </Route>
          <Route path="dashboard" element={<DashBoardLayout />}>
            <Route index element={<Navigate to={"users"} />} />
            <Route path="users" element={<DashBoardUser />} />
            <Route path="users/:id" element={<DashBoardUserDetail />} />
            <Route path="books" element={<DashBoardBook />} />
            <Route path="orders" element={<DashBoardOrder />} />
            <Route path="orders/:id" element={<DashBoardOrderDetail />} />
          </Route>
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
      <ToastContainer
        pauseOnHover={false}
        transition={Slide}
        pauseOnFocusLoss={false}
        autoClose={4000}
      />
    </ThemeProvider>
  );
};

export default App;
