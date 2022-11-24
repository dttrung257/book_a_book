import { useCallback, useEffect, useState } from "react";
//import { useNavigate } from "react-router-dom";
import axios, { isAxiosError } from "../../../apis/axiosInstance";
import { Table, Form } from "react-bootstrap";
import style from "../User/User.module.css"; // dung ke
import OrderItem from "./OrderItem";
import { useAppSelector } from "../../../store/hook";
import { OrderInfo } from "../../../models";
import { Button } from "@mui/material";
import { BsSearch } from "react-icons/bs";
import { useSearchParams } from "react-router-dom";
const Order = () => {

 //   const navigate = useNavigate();
    const accessToken = useAppSelector((state) => state.auth.accessToken);
    const [ordersList, setOrdersList] = useState<OrderInfo[]>([]);
    const [searchParams, setSearchParams] = useSearchParams();
    const getOrdersList = useCallback(
        async (page: number | string = 0) => {
          const response = await axios.get(`/manage/orders?page=${page}`, {
            headers: {
              Authorization: `Bearer ${accessToken}`,
            },
          });
          return response.data;
        },
        [accessToken]
      );


    useEffect(() => {
        const order = "";
        const page = searchParams.get("page") ||"0";

        if (!order) {
            getOrdersList(page)
              .then((data) => {
                console.log(data);
                setOrdersList(data.content);
                //setTotalPages(data.totalPages);
              })
              .catch((error) => {
                if (isAxiosError(error)) {
                  const data = error.response?.data;
                  console.log("error:", data);
                  //setMessage(data?.message);
                } else {
                  //setMessage("Unknow error!!!");
                  console.log(error);
                }
              });
          }
        return () => {};
    }, [getOrdersList, searchParams])
    return (
        <>
            <div className={`${style.header} mb-2`}>
                <h2> Orders list</h2>
                <Button
                style={{ backgroundColor: "var(--primary-color)", color: "white" }}
                onClick={() => {
                    //setShowAddModal(true);
                    console.log('add')
                }}
                >
                Add Order
                </Button>
            </div>
            <div className={`${style.content}`}>
              <div className="d-flex justify-content-between align-items-center">
                <h4>Result for {searchParams.get("orders") || "all orders"}</h4>
                <div
                    id={style.search}
                    className="px-3 py-2 d-flex justify-content-between align-items-center"
                    // onClick={() => setShowSearchModal(true)}
                >
                    Search for orders
                    <BsSearch />
                </div>
              </div>
              <div>
              <Table className="mt-4" hover responsive="md">
                  <thead className={`${style.tableHeader}`}>
                  <tr>
                      <th>ID</th>
                      <th>User</th>
                      <th>Date</th>
                      <th>Quantity</th>
                      <th>Total</th>
                      <th>Status</th>
                      <th></th>
                  </tr>
                  </thead>
                  <tbody className={`${style.tableBody}`}>
                      {ordersList.map((order) => (
                      <OrderItem
                      key={order.id}
                      order={order}
                      />
                      ))}
                  </tbody>
              </Table>
              </div>

              {/* To do Pagination */}
              <div>
                <button onClick={() => {
                  const page = searchParams.get("page") ||"0";
                  searchParams.set("page", (parseInt(page) - 1).toString());
                  setSearchParams(searchParams);
                  }
                 }>Prev</button>
                <button onClick={() => {
                  const page = searchParams.get("page") ||"0";
                  searchParams.set("page", (parseInt(page) + 1).toString());
                  setSearchParams(searchParams);
                }}>Next</button>
              </div>
            </div>
        </>
    );
  };
  
  export default Order;