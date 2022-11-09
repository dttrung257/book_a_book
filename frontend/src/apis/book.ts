import axiosInstance from "./axiosInstance";

export const getBookViaId = async (id: number) => {
  const response = await axiosInstance.get(`book/fetch_by_id/${id}`);
  return response.data;
};
