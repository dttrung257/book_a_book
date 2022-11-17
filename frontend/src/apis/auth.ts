import axiosInstance from "./axiosInstance";
import { UserSignUp, VerifyEmail } from "../models";
import { useAppSelector } from "../store/hook";

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
