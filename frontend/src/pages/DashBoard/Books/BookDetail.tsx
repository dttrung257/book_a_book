import { Button } from "@mui/material";
import { useAppSelector } from "../../../store/hook";
import { useEffect, useState } from "react";
import { FaStar } from "react-icons/fa";
import { useNavigate, useParams } from "react-router-dom";
import AppModal from "../../../components/AppModal/AppModal";
import axios, { isAxiosError } from "../../../apis/axiosInstance";
import { Book } from "../../../models";
import style from "../MainLayout.module.css";
import "./index.css";
import EditBookModal from "./EditBookModal";
const BookDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const [book, setBook] = useState<Book | null>(null);
  const { accessToken } = useAppSelector((state) => state.auth);
  const [editModal, setEditModal] = useState<boolean>(false);
  const [deleteModal, setDeleteModal] = useState<boolean>(false);
  useEffect(() => {
    const getInfo = async () => {
      try {
        const responseBook = await axios.get(`books/${id}`);
        console.log(responseBook.data);
        setBook(responseBook.data);
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
  }, [id, editModal]);
  const deleteBook = async () => {
    try {
      const res = await axios.delete(`manage/books/${id}`, {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      });
      console.log(res);

      navigate("/dashboard/books");
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
        <h2> Book detail</h2>
        <div>
          <Button
            style={{
              backgroundColor: "var(--primary-color)",
              color: "white",
              marginRight: "10px",
            }}
            onClick={() => {
              setEditModal(true);
              //   setMessage(null);
            }}
          >
            Edit Book
          </Button>
          <Button
            style={{ backgroundColor: "red", color: "white" }}
            onClick={() => {
              setDeleteModal(true);
              //setMessage(null);
            }}
          >
            Delete
          </Button>
        </div>
      </div>
      <div className={`${style.content}`}>
        {/* <div id="bookSpecification">
          <div style={{ display: "flex" }}>
            <div className="specificationTitle">Name</div>
            <div>
              <p>{book?.name}</p>
            </div>
          </div>
          <div style={{ display: "flex" }}>
            <div className="specificationTitle">Author</div>
            <div>
              <p>{book?.author !== null ? book?.author : "Unknown"}</p>
            </div>
          </div>
          <div style={{ display: "flex" }}>
            <div className="specificationTitle">Category</div>
            <div>
              <p>{book?.category !== null ? book?.category : "Unknown"}</p>
            </div>
          </div>
          <div style={{ display: "flex" }}>
            <div className="specificationTitle">Publisher</div>
            <div>
              <p>{book?.publisher !== null ? book?.publisher : "Unknown"}</p>
            </div>
          </div>
          <div style={{ display: "flex" }}>
            <div className="specificationTitle">Publication year</div>
            <div>
              <p>
                {book?.yearOfPublication !== null
                  ? book?.yearOfPublication
                  : "Unknown"}
              </p>
            </div>
          </div>
          <div style={{ display: "flex" }}>
            <div className="specificationTitle">ISBN</div>
            <div>
              <p>{book?.isbn !== null ? book?.isbn : "Unknown"}</p>
            </div>
          </div>
          <div style={{ display: "flex" }}>
            <div className="specificationTitle">Pages</div>
            <div>
              <p>
                {book?.numberOfPages !== null ? book?.numberOfPages : "Unknown"}
              </p>
            </div>
          </div>
          <div style={{ display: "flex" }}>
            <div className="specificationTitle">Dimensions</div>
            <div>
              <p>
                {book?.width !== null ? book?.width : "Unknown"} x{" "}
                {book?.height !== null ? book?.height : "Unknown"}
              </p>
            </div>
          </div>
          <div style={{ display: "flex" }}>
            <div className="specificationTitle">Stock</div>
            <div>
              <p>
                {book?.quantityInStock !== null
                  ? book?.quantityInStock
                  : "Unknown"}
              </p>
            </div>
          </div>
        </div> */}
        <h4 style={{ marginBottom: "20px" }}>Book ID #{book?.id}</h4>
        <div className="bookinfo d-flex justify-content-around">
          <div id="bookframe">
            <img src={book?.image} alt={book?.name} />
          </div>

          <div className="detail">
            <h5 className="fw-bold" style={{ marginBottom: "20px" }}>
              {book?.name.toUpperCase()}
            </h5>
            <div className="d-flex">
              <div className="fw-bold title">Author: &nbsp;</div>
              <div>{book?.author}</div>
            </div>
            <div>
              <div className="fw-bold title">Description: &nbsp;</div>
              <p id="book-description">{book?.description}</p>
            </div>
            <div className="d-flex">
              <div className="fw-bold title">Category:&nbsp;</div>
              <div>{book?.category}</div>
            </div>
            <div className="d-flex">
              <div className="fw-bold title">Ratings: &nbsp;</div>
              <div style={{ alignItems: "center", display: "flex" }}>
                <span style={{ marginRight: "5px" }}>
                  {book?.rating !== null ? book?.rating : 5}
                </span>
                <FaStar color="ffc107" />
              </div>
            </div>
            <div className="d-flex flex-row">
              <div className="d-flex flex-fill" style={{ minWidth: "350px" }}>
                <div className="fw-bold title">Selling Price: &nbsp;</div>
                <div>${book?.sellingPrice}</div>
              </div>
              <div className="d-flex flex-fill">
                <div className="fw-bold title">Buy Price: &nbsp;</div>
                <div>${book?.buyPrice}</div>
              </div>
            </div>
            <div className="d-flex flex-row">
              <div className="d-flex flex-fill" style={{ minWidth: "350px" }}>
                <div className="fw-bold title">Sold: &nbsp;</div>
                <div>{book?.quantitySold}</div>
              </div>
              <div className="d-flex flex-fill">
                <div className="fw-bold title">Available: &nbsp;</div>
                <div>{book?.quantityInStock}</div>
              </div>
            </div>
            <div className="d-flex flex-row">
              <div
                className="d-flex flex-fill flex-column"
                style={{ minWidth: "350px" }}
              >
                <div className="d-flex">
                  <div className="fw-bold title">ISBN: &nbsp;</div>
                  <div>{book?.isbn ? book?.isbn : "Unknown"}</div>
                </div>

                <div className="d-flex">
                  <div className="fw-bold title">Publisher: &nbsp;</div>
                  <div>{book?.publisher ? book?.publisher : "Unknown"}</div>
                </div>
                <div className="d-flex">
                  <div className="fw-bold title">Publication Year: &nbsp;</div>
                  <div>
                    {book?.yearOfPublication
                      ? book?.yearOfPublication
                      : "Unknown"}
                  </div>
                </div>
              </div>
              <div className="d-flex flex-fill flex-column">
                <div className="d-flex">
                  <div className="fw-bold title">Dimensions: &nbsp;</div>
                  <div>
                    {book?.width ? book?.width : "Unknown"} x{" "}
                    {book?.height ? book?.height : "Unknown"}
                  </div>
                </div>
                <div className="d-flex">
                  <div className="fw-bold title">Pages: &nbsp;</div>
                  <div>
                    {book?.numberOfPages ? book?.numberOfPages : "Unknown"}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <AppModal
        title="Delete "
        showModal={deleteModal}
        setShowModal={setDeleteModal}
      >
        <div className={`${style.deleteModal}`}>
          <p>Delete book #{id} ?</p>
          {/* {message ? (
            <div
              style={{
                color: message.status === "fail" ? "red" : "green",
              }}
            >
              {message?.message}
            </div>
          ) : null} */}
          <div>
            <Button
              className={`${style.deleteBtn} float-end`}
              onClick={() => {
                deleteBook();
                console.log("delete");
              }}
            >
              Confirm
            </Button>
          </div>
        </div>
      </AppModal>
      {book && (
        <EditBookModal
          book={book as Book}
          showModal={editModal}
          setShowModal={setEditModal}
        />
      )}
    </>
  );
};

export default BookDetail;
