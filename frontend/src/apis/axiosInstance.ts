import axios from "axios";

export const isAxiosError = axios.isAxiosError;

export default axios.create({
  baseURL: "//localhost:9090/api",
  headers: {
    "Content-Type": "application/json",
  },
});
