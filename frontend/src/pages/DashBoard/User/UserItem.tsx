import React, { useState } from "react";
import { SlSettings } from "react-icons/sl";
import { Button } from "@mui/material";
import style from "./User.module.css";
import { UserDetailInfo } from "../../../models";
import { Link } from "react-router-dom";
import AppModal from "../../../components/AppModal/AppModal";
import axios, { isAxiosError } from "../../../apis/axiosInstance";
import { useAppSelector } from "../../../store/hook";

interface MessageStatus {
  status: "success" | "fail" | "";
  message: string;
}

const UserItem = ({
  user: userInfo,
}: {
  user: UserDetailInfo;
}) => {
  const [locked, setLocked] = useState<boolean>(userInfo.locked);
  const [lockModal, setLockModal] = useState<boolean>(false);
  const [message, setMessage] = useState<MessageStatus | null>(null);
  const {accessToken, user} = useAppSelector((state) => state.auth);

  const closeModal = (show: boolean) => {
    setLockModal(show);
    setMessage(null);
  };

  const toggleLockUser = async () => {
    if (userInfo.authority === "ADMIN") {
      return setMessage({
        status: "fail",
        message: "Can not change this user status!",
      });
    }
    //TODO: change req body: state
    try {
      setMessage(null);
      if (locked) {
        const res = await axios.put(
          `manage/users/${userInfo.id}/status`,
          {
            status: "locked",
            state: "true",
          },
          {
            headers: {
              Authorization: `Bearer ${accessToken}`,
            },
          }
        );

        setLocked(false);
        setMessage({
          status: "success",
          message: res.data,
        });
      } else {
        const res = await axios.put(
          `manage/users/${userInfo.id}/status`,
          {
            status: "activated",
            state: "true",
          },
          {
            headers: {
              Authorization: `Bearer ${accessToken}`,
            },
          }
        );

        setLocked(true);
        setMessage({
          status: "success",
          message: res.data,
        });
      }
    } catch (error) {
      if (isAxiosError(error)) {
        const data = error.response?.data;

        setMessage({
          status: "fail",
          message: data.message,
        });
      } else {
        setMessage({
          status: "fail",
          message: "Unknow error!!!",
        });
        console.log(error);
      }
    }
  };

  return (
    <>
      <tr>
        <td>
          <span>{userInfo.id}</span>
        </td>
        <td>{userInfo.firstName + " " + userInfo.lastName}</td>
        <td>{userInfo.gender}</td>
        <td>{userInfo.email}</td>
        <td>{userInfo.phoneNumber || "Not updated"}</td>
        <td>{userInfo.authority}</td>
        <td>{locked ? "Locked" : "Active"}</td>
        <td className={style.iconSetting}>
          <SlSettings />
          <div className={style.action}>
            <div>
              <Link to={`/dashboard/users/${userInfo.id}`}>Details</Link>
            </div>
            <div className={style.divider}></div>
            <div onClick={() => setLockModal(true)}>
              {locked ? "Unlock" : "Lock"}
            </div>
          </div>
          <div>
            <AppModal
              title={`${userInfo.locked ? "Unlock" : "Lock"} user ${
                userInfo.id
              }`}
              showModal={lockModal}
              setShowModal={closeModal}
            >
              {message ? (
                <div
                  style={{
                    color: message.status === "fail" ? "red" : "green",
                  }}
                >
                  {message?.message}
                </div>
              ) : null}
              <div
                className={`${style.lockModal} d-flex justify-content-end mt-3`}
              >
                <Button
                  className={style.cancelBtn}
                  onClick={() => closeModal(false)}
                >
                  Cancel
                </Button>
                <Button
                  className={`${style.toggleLockBtn}`}
                  onClick={() => toggleLockUser()}
                >
                  Confirm
                </Button>
              </div>
            </AppModal>
          </div>
        </td>
      </tr>
    </>
  );
};

export default UserItem;
