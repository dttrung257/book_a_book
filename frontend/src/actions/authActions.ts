import { createAsyncThunk } from "@reduxjs/toolkit";
import { AxiosError } from "axios";
import axios from "../apis/axios";
import AuthInfo from "../models/authInfo.model";

export const login = createAsyncThunk(
	"auth/login",
	async ({ email, password }: { email: string; password: string }, thunkAPI) => {
		try {
			//TODO: change api url, get response data
			const response = await axios.post(
				"/accounts:signInWithPassword",
				{
					email: email,
					password: password,
					returnSecureToken: true,
				},
				{
					params: {
						key: "AIzaSyDvI7V7DImXkf2rol7UJLJOQU7wq_4i-qQ",
					},
				}
			);
			let data = response.data;
			console.log("@@ @@ ", data);

			data = {
				user: {
					firstName: "Pham",
					lastName: "Quang",
					role: ["ADMIN"],
				},
				accessToken: response.data.idToken,
			};
			return data;
		} catch (error) {
			if (error instanceof AxiosError) {
				thunkAPI.rejectWithValue(error.response!!.data);
			}
			console.log(error);
		}
	}
);

export const signup = createAsyncThunk("auth/signup", async (info: AuthInfo, thunkAPI) => {
	try {
		//TODO: change api url
		const response = await axios.post(
			"/accounts:signUp",
			{
				email: info.email,
				password: info.password,
				returnSecureToken: true,
			},
			{
				headers: {
					"Content-Type": "application/json",
				},
				params: {
					key: "AIzaSyDvI7V7DImXkf2rol7UJLJOQU7wq_4i-qQ",
				},
			}
		);
		let data = response.data;
		return data;
	} catch (error) {
		if (error instanceof AxiosError) {
			thunkAPI.rejectWithValue(error.response!!.data);
		}
		console.log(error);
	}
});
