import axiosInstance from "./axiosInstance";
import { changePass, UserDetail, UserSignUp, VerifyEmail } from "../models";

export const verifyEmail = async (verify: VerifyEmail) => {
  const res = await axiosInstance.get(
    `/users/forgot_password/${verify.email}/confirm_verification/${verify.verifyCode}`
  );
  return res.data;
};

export const signUp = async (info: UserSignUp) => {
  const response = await axiosInstance.post("/authen/register", info);
  return response.data;
};

export const getUserInfo = async (o: Object) => {
  const response = await axiosInstance.get("/users", o);
  return response.data;
};

export const updateUserInfo = async (user: UserDetail, o: Object) => {
  const response = await axiosInstance.put("/users", user, o);
  return response.data;
};

export const changeUserPassword = async (changePass: changePass, o: Object) => {
  const response = await axiosInstance.put(
    "/users/change_password",
    changePass,
    o
  );
  return response;
};
