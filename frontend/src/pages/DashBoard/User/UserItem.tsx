import React, { useState } from "react";
import { SlSettings } from "react-icons/sl";
import { Button } from "@mui/material";
import { toast } from "react-toastify";
import style from "./User.module.css";
import { UserDetailInfo } from "../../../models";
import { Link } from "react-router-dom";
import AppModal from "../../../components/AppModal/AppModal";
import axios, { isAxiosError } from "../../../apis/axiosInstance";
import { useAppSelector } from "../../../store/hook";

const UserItem = ({ user: userInfo }: { user: UserDetailInfo }) => {
  const [locked, setLocked] = useState<boolean>(userInfo.locked);
  const [modal, setModal] = useState<boolean>(false);
  const [modalTitle, setModalTitle] = useState<string>("");
  const [modalType, setModalType] = useState<"LOCK" | "ACTIVATE">("LOCK");
  const [errMessage, setErrMessage] = useState<string>("");
  const { accessToken, user } = useAppSelector((state) => state.auth);

  console.log(userInfo.emailVerified, userInfo.email, userInfo.locked);

  const closeModal = (show: boolean) => {
    setModal(show);
    setErrMessage("");
  };

  const toggleLockUser = async () => {
    if (userInfo.authority === "ADMIN") {
      return toast.error("Can not change this user status!");
    }

    try {
      setErrMessage("");

      const res = await axios.put(
        `manage/users/${userInfo.id}/status`,
        {
          status: "locked",
          state: !locked,
        },
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );

      setLocked(!locked);
      closeModal(false);
      toast.success(res.data);
    } catch (error) {
      if (isAxiosError(error)) {
        const data = error.response?.data;
        setErrMessage(data?.message);
      } else {
        setErrMessage("Unknow error!!!");
      }
      console.log(error);
    }
  };

  const activateUser = async () => {
    try {
      if (userInfo.emailVerified === true) {
        return toast.error("This user has been activated!");
      }

      const res = await axios.put(
        `manage/users/${userInfo.id}/status`,
        {
          status: "activated",
          state: true,
        },
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );
      console.log(res);

      closeModal(false);
      toast.success(res.data);
    } catch (error) {
      if (isAxiosError(error)) {
        const data = error.response?.data;
        setErrMessage(data?.message);
      } else {
        setErrMessage("Unknow error!!!");
      }
      console.log(error);
    }
  };

  const showToggleLockUserModal = () => {
    setModalTitle(`${userInfo.locked ? "Unlock" : "Lock"} user ${userInfo.id}`);
    setModalType("LOCK");
    setModal(true);
  };

  const showActivateUserModal = () => {
    setModalTitle(`Active user ${userInfo.id}`);
    setModalType("ACTIVATE");
    setModal(true);
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
            <div onClick={showToggleLockUserModal}>
              {locked ? "Unlock" : "Lock"}
            </div>
            <div className={style.divider}></div>
            <div onClick={showActivateUserModal}>Activate</div>
          </div>
          <div>
            <AppModal
              title={modalTitle}
              showModal={modal}
              setShowModal={closeModal}
            >
              <div style={{ color: "red" }}>{errMessage}</div>
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
                  onClick={() =>
                    modalType === "LOCK" ? toggleLockUser() : activateUser()
                  }
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
