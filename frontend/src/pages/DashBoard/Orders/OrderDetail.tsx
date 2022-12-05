//import { useParams } from "react-router-dom";

import { Avatar, Button } from "@mui/material";
import { useEffect, useState } from "react";
import { Form, Table } from "react-bootstrap";
import { useNavigate, useParams } from "react-router-dom";
import styled from "styled-components";
import axios, { isAxiosError } from "../../../apis/axiosInstance";
import AppModal from "../../../components/AppModal/AppModal";
import { OrderInfo, UserDetailInfo } from "../../../models";
import { useAppSelector } from "../../../store/hook";
import style from "../User/User.module.css"; // dung ke

const SubContainer = styled.div`
  border-bottom: solid 2px #cbcbcb;
  display: flex;
  flex-direction: column;
  margin-top: 5px;
`;

interface OrderDetail {
  id: string;
  image: string;
  bookName: string;
  quantityOrdered: number;
  priceEach: number;
}

interface MessageStatus {
  status: "success" | "fail" | "";
  message: string;
}

const OrderDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { accessToken } = useAppSelector((state) => state.auth);
  const [orderInfo, setOrderInfo] = useState<OrderInfo | null>(null);
  const [orderDetails, setOrderDetails] = useState<OrderDetail[]>([]);
  const [userInfo, setUserInfo] = useState<UserDetailInfo | null>(null);
  const [statusModal, setStatusModal] = useState<boolean>(false);
  const [deleteModal, setDeleteModal] = useState<boolean>(false);
  const [orderStatus, setOrderStatus] = useState<string>("");
  const [message, setMessage] = useState<MessageStatus | null>(null);
  //const [newStatus, setNewStatus] = useState<string>('');
  useEffect(() => {
    const getInfo = async () => {
      try {
        const responseOrder = await axios.get(`manage/orders/${id}`, {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        });
        console.log("order: ", responseOrder.data);
        setOrderInfo(responseOrder.data);

        // order status?
        setOrderStatus(responseOrder.data.status);

        const responseOrderDetails = await axios.get(
          `/orders/${id}/orderdetails`,
          {
            headers: {
              Authorization: `Bearer ${accessToken}`,
            },
          }
        );
        console.log("orderdetails: ", responseOrderDetails.data.content);
        setOrderDetails(responseOrderDetails.data.content);
        if (responseOrder.data.userId) {
          const responseUser = await axios.get(
            `manage/users/${responseOrder.data.userId}`,
            {
              headers: {
                Authorization: `Bearer ${accessToken}`,
              },
            }
          );
          console.log(responseUser.data);
          setUserInfo(responseUser.data);
        }
      } catch (error) {
        if (isAxiosError(error)) {
          const data = error.response?.data;
          //setErrMessage(data?.message);
          console.log("error: ", error);
        } else {
          //setErrMessage("Unknow error!!!");
          console.log("error: ", error);
        }
      }
    };

    getInfo();
    return () => {};
  }, [accessToken, id]);

  const changeOrderStatus = async (status: string) => {
    try {
      const res = await axios.put(
        `manage/orders/${id}`,
        {
          status: status,
        },
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );
      //setOrderStatus(status);
      setOrderInfo({ ...orderInfo, status } as OrderInfo);
      setStatusModal(false);
      console.log(res);
    } catch (error) {
      if (isAxiosError(error)) {
        const data = error.response?.data;
        //setErrMessage(data?.message);
      } else {
        //setErrMessage("Unknow error!!!");
        console.log(error);
      }
    }
  };

  const deleteOrder = async () => {
    if (
      orderInfo &&
      (orderInfo.status === "SUCCESS" || orderInfo.status === "SHIPPING")
    ) {
      setMessage({
        status: "fail",
        message: "Can not delete this order!",
      });
      return;
    }
    try {
      const res = await axios.delete(`manage/orders/${id}`, {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      });
      console.log(res);

      navigate("/dashboard/orders");
    } catch (error) {
      if (isAxiosError(error)) {
        const data = error.response?.data;
        //setErrMessage(data?.message);
      } else {
        //setErrMessage("Unknow error!!!");
        console.log(error);
      }
    }
  };

  return (
    <>
      <div className={`${style.header} mb-2`}>
        <h2> Order detail</h2>
        <div>
          <Button
            style={{
              backgroundColor: "var(--primary-color)",
              color: "white",
              marginRight: "10px",
            }}
            onClick={() => {
              setStatusModal(true);
              setMessage(null);
            }}
          >
            Change Status
          </Button>
          <Button
            style={{ backgroundColor: "red", color: "white" }}
            onClick={() => {
              setDeleteModal(true);
              setMessage(null);
            }}
          >
            Delete
          </Button>
        </div>
      </div>
      <div
        className={`${style.content} d-flex flex-column`}
        style={{ minHeight: "85vh" }}
      >
        <SubContainer>
          <h5>Order ID #{orderInfo?.id}</h5>
          <div className="d-flex mx-3">
            <div className="flex-fill">
              <p>
                <span className="fw-bold">Status: </span>
                {orderInfo?.status}
              </p>
              <p>
                <span className="fw-bold">Order Date: </span>
                {orderInfo?.orderDate}
              </p>
            </div>
            <div className="flex-fill">
              <p>
                <span className="fw-bold">Total: </span>${orderInfo?.total}
              </p>
              <p>
                <span className="fw-bold">Quantity: </span>
                {orderInfo?.quantity}
              </p>
            </div>
          </div>
        </SubContainer>
        {orderInfo?.userId && (
          <SubContainer
            className="flex-row pb-1"
            style={{ minHeight: "125px" }}
          >
            <div
              className="flex-fill ps-2"
              style={{ borderRight: "solid 2px #cbcbcb", maxWidth: "50%" }}
            >
              <h5>Customer</h5>
              <div className="d-flex">
                <Avatar
                  src={userInfo?.avatar}
                  style={{
                    width: 70,
                    height: 70,
                  }}
                  alt="avatar"
                />
                <div className="ms-3">
                  <span className="fw-bold">
                    {userInfo?.lastName} {userInfo?.firstName}
                  </span>
                  <br />
                  <span>#{userInfo?.id}</span>
                  <br />
                  <span>{userInfo?.email}</span>
                </div>
              </div>
            </div>
            <div className="flex-fill ps-3">
              <h5>Shipping Address</h5>
              <p className="fw-bold">{orderInfo.fullName}</p>
              <p>{orderInfo.address}</p>
            </div>
          </SubContainer>
        )}
        <div className="mt-3">
          <h5>Items</h5>
          <Table>
            <thead>
              <tr>
                <th>No.</th>
                <th>Image</th>
                <th>Product</th>
                <th>Price Each</th>
                <th>Quantity</th>
                <th>Subtotal</th>
              </tr>
            </thead>
            <tbody>
              {orderDetails.map((order, index) => (
                <tr key={order.id}>
                  <td>{index + 1}</td>
                  <td>
                    <img
                      src={order.image}
                      alt="bookimg"
                      style={{ height: "60px" }}
                    />
                  </td>
                  <td>{order.bookName}</td>
                  <td>${order.priceEach}</td>
                  <td>{order.quantityOrdered}</td>
                  <td>${order.priceEach * order.quantityOrdered}</td>
                </tr>
              ))}
              <tr className="fs-5 lh-lg">
                <td colSpan={5} className="fw-bold text-start">
                  Total
                </td>
                <td>${orderInfo?.total}</td>
              </tr>
            </tbody>
          </Table>
        </div>
      </div>

      <AppModal
        title="Change status "
        showModal={statusModal}
        setShowModal={setStatusModal}
      >
        <div className={`${style.deleteModal}`} style={{ width: "30vw" }}>
          <p>Current status: {orderInfo?.status}</p>
          <div className="my-3">
            <Form.Group controlId="formBasicSelect">
              <Form.Label>Select Status</Form.Label>
              <Form.Control
                as="select"
                value={orderStatus}
                onChange={(e) => {
                  setOrderStatus(e.target.value);
                  console.log(e.target.value);
                }}
              >
                <option value="PENDING">PENDING</option>
                <option value="SHIPPING">SHIPPING</option>
                <option value="SUCCESS">SUCCESS</option>
                <option value="CANCELED">CANCELED</option>
              </Form.Control>
            </Form.Group>
          </div>

          <div className="mt-3">
            <Button
              className={`${style.deleteBtn} float-end`}
              onClick={() => {
                // deleteOrder();
                changeOrderStatus(orderStatus);
              }}
            >
              Confirm
            </Button>
          </div>
        </div>
      </AppModal>

      <AppModal
        title="Delete "
        showModal={deleteModal}
        setShowModal={setDeleteModal}
      >
        <div className={`${style.deleteModal}`}>
          <p>Delete order {id} ?</p>
          {message ? (
            <div
              style={{
                color: message.status === "fail" ? "red" : "green",
              }}
            >
              {message?.message}
            </div>
          ) : null}
          <div>
            <Button
              className={`${style.deleteBtn} float-end`}
              onClick={() => {
                deleteOrder();
                console.log("delete");
              }}
            >
              Confirm
            </Button>
          </div>
        </div>
      </AppModal>
    </>
  );
};

export default OrderDetail;
