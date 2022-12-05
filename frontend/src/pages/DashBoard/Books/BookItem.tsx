import { Book } from "../../../models";
import { CgMoreVertical } from "react-icons/cg";
import { Link } from "react-router-dom";
import Form from "react-bootstrap/Form";
import { useState } from "react";
import { Avatar, Button } from "@mui/material";
import { useAppSelector } from "../../../store/hook";
import AppModal from "../../../components/AppModal/AppModal";
import axios, { isAxiosError } from "../../../apis/axiosInstance";
import style from "../MainLayout.module.css";
const BookItem = ({ book: bookInfo }: { book: Book }) => {
  const { accessToken } = useAppSelector((state) => state.auth);
  const [statusModal, setStatusModal] = useState<boolean>(false);
  const [isStopSelling, setIsStopSelling] = useState<boolean>(
    bookInfo.stopSelling
  );
  const toggleBookStatus = () => {
    setStatusModal(true);
  };
  const changeBookStatus = async () => {
    setIsStopSelling(!isStopSelling);
    try {
      const response = await axios.put(
        `manage/books/${bookInfo.id}/status`,
        {
          stopSelling: !isStopSelling,
        },
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );
      setIsStopSelling(!isStopSelling);
      setStatusModal(false);
    } catch (error) {
      if (isAxiosError(error)) {
        const data = error.response?.data;
        //setErrMessage(data?.message);
      } else {
        // setErrMessage("Unknow error!!!");
      }
      console.log(error);
    }
  };
  return (
    <>
      <tr>
        <td>{bookInfo.id}</td>
        <td>
          <img
            src={bookInfo.image}
            alt="bookimg"
            style={{ maxHeight: "50px" }}
          />
        </td>
        <td style={{ textAlign: "left" }}>{bookInfo.name}</td>
        {/* <td>{bookInfo.category}</td> */}
        <td>${bookInfo.sellingPrice}</td>
        <td>{bookInfo.availableQuantity}</td>
        <td>{bookInfo.quantitySold}</td>
        <td>{bookInfo.rating || 5}/5</td>
        <td>
          <Form>
            <Form.Switch onChange={toggleBookStatus} checked={!isStopSelling} />
          </Form>
          <AppModal
            title={`Change status of book #${bookInfo.id} to ${
              isStopSelling ? "Selling" : "Stop Selling"
            }`}
            showModal={statusModal}
            setShowModal={setStatusModal}
          >
            {/* <div style={{ color: "red" }}>{errMessage}</div> */}
            <div
              className={`${style.lockModal} d-flex justify-content-end mt-3`}
            >
              <Button
                className={style.cancelBtn}
                onClick={() => setStatusModal(false)}
              >
                Cancel
              </Button>
              <Button
                className={`${style.toggleLockBtn}`}
                onClick={changeBookStatus}
              >
                Confirm
              </Button>
            </div>
          </AppModal>
        </td>
        {/* <td>{bookInfo.stopSelling ? <BsToggleOff/> : <BsToggleOn/>}</td> */}
        <td>
          <Link to={`/dashboard/books/${bookInfo.id}`} title="More">
            <CgMoreVertical color="black" />
          </Link>
        </td>
      </tr>
    </>
  );
};

export default BookItem;
