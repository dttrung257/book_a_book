import axiosInstance from "./axiosInstance";

export const getBookViaId = async (id: number) => {
  const response = await axiosInstance.get(`book/fetch_by_id/${id}`);
  return response.data;
};

export const getAllBook = async() => {
  try{
    const response = await axiosInstance.get('book/fetch_books',);
    return response.data;
  }
  catch(error) {
    console.log(error);
  }
};