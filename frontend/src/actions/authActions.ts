import { createAsyncThunk } from "@reduxjs/toolkit";
import axios from "../apis/axios";

export const login = createAsyncThunk(
	"auth/login",
	async ({ email, password }: { email: string; password: string }, thunkAPI) => {
		try {
			const response = await axios.post("/authen/sign_in", {
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
