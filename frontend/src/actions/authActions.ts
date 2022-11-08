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
      let data = response.data;
      console.log("@@ @@ ", data);

      data = {
        user: {
          firstName: data.firstName,
          lastName: data.lastName,
          role: data.authorities,
        },
        accessToken: data.accessToken,
      };
      return data;
    } catch (error) {
      return thunkAPI.rejectWithValue(error);
    }
  }
);
