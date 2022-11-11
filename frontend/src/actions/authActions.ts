import { createAsyncThunk } from "@reduxjs/toolkit";
import axiosInstance from "../apis/axiosInstance";

export const login = createAsyncThunk(
  "auth/login",
  async (
    { email, password }: { email: string; password: string },
    thunkAPI
  ) => {
    try {
      console.log(email, password);
      const response = await axiosInstance.post("/authen/sign_in", {
        email: email,
        password: password,
      });
      const data = response.data;
      console.log("@@ @@ ", data);

      const info = {
        user: {
          avatar: data.avatar,
          firstName: data.firstName,
          lastName: data.lastName,
          authority: data.authority,
        },
        accessToken: data.accessToken,
      };
      return info;
    } catch (error) {
      return thunkAPI.rejectWithValue(error);
    }
  }
);
