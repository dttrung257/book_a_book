import { access } from "fs";
import axiosInstance from "./axiosInstance";

export const getOrder = async (accessToken: string) => {
  const response = await axiosInstance.get("orders", {
    headers: {
      Authorization: `Bearer ${accessToken}`,
    },
  });
  return response.data;
};
export const deleteOrder = async (accessToken: string, id: string) => {
  const response = await axiosInstance.delete(`orders/${id}`, {
    headers: {
      Authorization: `Bearer ${accessToken}`,
    },
  });
};
