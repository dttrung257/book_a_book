import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { Button, Avatar } from "@mui/material";
import { Table, Form } from "react-bootstrap";
import style from "./User.module.css";
import { UserDetailInfo } from "../../../models";
import AppModal from "../../../components/AppModal/AppModal";
import axios, { isAxiosError } from "../../../apis/axiosInstance";
import { useAppSelector } from "../../../store/hook";
import PasswordError, { checkPassword } from "../../../utils/checkPassword";

interface Order {
  id: string;
  address: string;
  order_date: string;
  status: string;
  user_id: string;
}

const UserDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { accessToken, user } = useAppSelector((state) => state.auth);
  const [userInfo, setUserInfo] = useState<UserDetailInfo | null>(null);
  const [orders, setOrders] = useState<Order[]>([]);
  const [deleteModal, setDeleteModal] = useState<boolean>(false);
  const [editModal, setEditModal] = useState<boolean>(false);
  const [newPassword, setNewPassword] = useState<string>("");
  const [confirmNewPassword, setConfirmNewPassword] = useState<string>("");
  const [editPassErrMessage, setEditPassErrMessage] = useState<PasswordError>(
    {}
  );
  const [errMessage, setErrMessage] = useState<string>("");

  useEffect(() => {
    const getInfo = async () => {
      try {
        const responseUser = await axios.get(`manage/users/${id}`, {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        });
        setUserInfo(responseUser.data);
        console.log(responseUser.data);

        const responseOrder = await axios.get(
          `manage/orders?user_id=${responseUser.data.id}`,
          {
            headers: {
              Authorization: `Bearer ${accessToken}`,
            },
          }
        );
        console.log(responseOrder.data.content);
        setOrders(responseOrder.data.content);
      } catch (error) {
        if (isAxiosError(error)) {
          const data = error.response?.data;
          setErrMessage(data?.message);
        } else {
          setErrMessage("Unknow error!!!");
          console.log(error);
        }
      }
    };

    getInfo();
    return () => {};
  }, [accessToken, id]);

  const deleteUser = async () => {
    if (userInfo && userInfo.authority === "ADMIN") {
      return toast.error("Can not delete this user!");
    }

    try {
      const res = await axios.delete(`manage/users/${id}`, {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      });
      console.log(res);

      toast.success(
        `User ${
          userInfo?.firstName + " " + userInfo?.lastName
        } has been deleted`
      );
      navigate(-1);
    } catch (error) {
      if (isAxiosError(error)) {
        const data = error.response?.data;
        toast.error(data?.message);
      } else {
        toast.error("Unknow error!!!");
        console.log(error);
      }
    }
  };

  const editPassword = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    try {
      const passwordError = checkPassword(newPassword, confirmNewPassword);
      setEditPassErrMessage(passwordError);
      if (passwordError && Object.keys(passwordError).length !== 0) return;

      const response = await axios.put(
        `/manage/users/${userInfo?.id}/password`,
        {
          newPassword: newPassword,
        },
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );
      console.log(response);

      toast.success("Change password successfully");
      setEditModal(false);
    } catch (error) {
      if (isAxiosError(error)) {
        const data = error.response?.data;
        toast.error(data?.message);
      } else {
        toast.error("Unknow error!!!");
        console.log(error);
      }
    }
  };

  return (
    <div id={style.userDetail}>
      <div className={`${style.header} mb-2`}>
        <h2>User Profile</h2>
        <div>
          {/* <Button
            style={{ backgroundColor: "var(--primary-color)", color: "white" }}
          >
            Save
          </Button> */}
          <Button variant="contained" onClick={() => setEditModal(true)}>
            Edit password
          </Button>
          <Button
            variant="contained"
            color="error"
            onClick={() => setDeleteModal(true)}
          >
            Delete
          </Button>
        </div>
      </div>
      <div className={`${style.content} d-flex flex-wrap`}>
        <div className={`${style.left} pe-3`}>
          <div
            className={`${style.avatar} d-flex flex-column align-items-center`}
          >
            <div className={`mb-3`}>
              <Avatar
                src={userInfo?.avatar}
                style={{
                  minWidth: 200,
                  minHeight: 200,
                }}
                alt="avatar"
              />
            </div>
            <div>
              <h3>
                {userInfo &&
                  `${userInfo?.firstName + " " + userInfo?.lastName}`}
              </h3>
            </div>
            <div>
              Status:{" "}
              <p
                style={{
                  color: userInfo && userInfo.locked ? "red" : "greenyellow",
                }}
              >
                {userInfo && userInfo.locked ? "Locked" : "Active"}
              </p>
            </div>
          </div>
          <div className={`${style.info} d-flex flex-column`}>
            <p className={`${style.title}`}>Information</p>
            <div>
              <h5>Email</h5>
              <p>{userInfo && userInfo?.email}</p>
            </div>
            <div>
              <h5>Phone number</h5>
              <p>
                {userInfo && userInfo?.phoneNumber
                  ? userInfo?.phoneNumber
                  : "Not updated"}
              </p>
            </div>
            <div>
              <h5>Gender</h5>
              <p>{userInfo && userInfo?.gender}</p>
            </div>
            <div>
              <h5>Address</h5>
              <p>
                {userInfo && userInfo?.address
                  ? userInfo?.address
                  : "Not updated"}
              </p>
            </div>
            <div>
              <h5>Registered</h5>
              <p>{userInfo && userInfo?.createdAt}</p>
            </div>
          </div>
        </div>
        <div className={`${style.right} ps-3 flex-grow-1`}>
          <div className={`${style.order}`}>
            <p className={`${style.title}`}>Overview</p>
            <div className={` d-flex flex-wrap flex-column mx-3`}>
              <div>
                <h5>Ordered</h5>
                <p>{orders.length} orders</p>
              </div>
              <div>
                <h5>Last order</h5>
                <p>11/11/1111 - #123</p>
              </div>
            </div>
          </div>
          <div>
            <p className={`${style.title}`}>Orders history</p>
            {orders.length > 0 ? (
              <Table bordered hover>
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Order date</th>
                    <th>Status</th>
                    <th>Address</th>
                  </tr>
                </thead>
                <tbody>
                  {orders.map((order) => (
                    <tr key={order.id}>
                      <td>{order.id}</td>
                      <td>{order.order_date}</td>
                      <td>{order.status}</td>
                      <td>{order.address}</td>
                    </tr>
                  ))}
                </tbody>
              </Table>
            ) : (
              <p>Not ordered</p>
            )}
          </div>
        </div>
      </div>
      <AppModal
        title="Delete "
        showModal={deleteModal}
        setShowModal={setDeleteModal}
      >
        <div className={`${style.deleteModal}`}>
          <p>Delete user {id} ?</p>
          <div>
            <Button
              className={`${style.deleteBtn} float-end`}
              onClick={() => {
                deleteUser();
              }}
            >
              Confirm
            </Button>
          </div>
        </div>
      </AppModal>
      <AppModal
        title="Edit password"
        showModal={editModal}
        setShowModal={setEditModal}
      >
        <div style={{ minWidth: "500px" }}>
          <Form onSubmit={editPassword}>
            <Form.Group className="mb-3" controlId="newPassword">
              <Form.Label>New password</Form.Label>
              <Form.Control
                type="text"
                placeholder="Enter new password"
                value={newPassword}
                onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                  setNewPassword(e.target.value.trim())
                }
              />
              {editPassErrMessage?.password ? (
                <Form.Text className="text-danger">
                  {editPassErrMessage.password}
                </Form.Text>
              ) : null}
            </Form.Group>

            <Form.Group className="mb-3" controlId="confirmNewPassword">
              <Form.Label>Confirm password</Form.Label>
              <Form.Control
                type="text"
                placeholder="Confirm password"
                value={confirmNewPassword}
                onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                  setConfirmNewPassword(e.target.value.trim())
                }
              />
              {editPassErrMessage?.confirmPassword ? (
                <Form.Text className="text-danger">
                  {editPassErrMessage.confirmPassword}
                </Form.Text>
              ) : null}
            </Form.Group>

            <div className="float-end">
              <Button variant="contained" type="submit">
                Submit
              </Button>
            </div>
          </Form>
        </div>
      </AppModal>
    </div>
  );
};

export default UserDetail;
