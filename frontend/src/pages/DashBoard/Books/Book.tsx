import { useCallback, useEffect, useState } from "react";
//import { useNavigate } from "react-router-dom";
import axios, { isAxiosError } from "../../../apis/axiosInstance";
import { Table, Form } from "react-bootstrap";
import style from "../User/User.module.css"; // dung ke
import BookItem from "./BookItem";
import { useAppSelector } from "../../../store/hook";
import { Book } from "../../../models";
import { Button } from "@mui/material";
import { BsSearch } from "react-icons/bs";
import { useSearchParams } from "react-router-dom";
import AppModal from "../../../components/AppModal/AppModal";

import Pagination from "@mui/material/Pagination";
import AddBookModal from "./AddBookModal";

interface SearchInfo {
  name?: string;
  category?: string;
  priceFrom?: number | string;
  priceTo?: number | string;
  rating?: number | string;
}

const Books = () => {
  const accessToken = useAppSelector((state) => state.auth.accessToken);
  const [booksList, setBooksList] = useState<Book[]>([]);
  const [showSearchModal, setShowSearchModal] = useState<boolean>(false);
  const [showAddModal, setShowAddModal] = useState<boolean>(false);
  const [searchParams, setSearchParams] = useSearchParams();
  const [searchInfo, setSearchInfo] = useState<SearchInfo>({});
  const [message, setMessage] = useState<string>("");
  const [totalpage, setTotalPages] = useState<number>(0);
  const [currentPage, setCurrentPage] = useState<number>(
    parseInt(searchParams.get("page") || "0")
  );
  // const getBooksList = useCallback(
  //   async (page: number | string = 0) => {
  //     const response = await axios.get(`/books?page=${page}`, {
  //       headers: {
  //         Authorization: `Bearer ${accessToken}`,
  //       },
  //     });
  //     return response.data;
  //   },
  //   [accessToken]
  // );

  const getBooksList = useCallback(
    async (filter: SearchInfo, page: number | string = 0) => {
      const response = await axios.get(
        `/books/?page=${page}&name=${filter.name}&category=${filter.category}&from=${filter.priceFrom}&to=${filter.priceTo}&rating=${filter.rating}`
      );
      return response.data;
    },
    [accessToken]
  );
  const getFilteredBooks = useCallback(
    async (filter: SearchInfo, page: number | string = 0) => {
      const response = await axios.get(
        `/books/?page=${page}&name=${filter.name}&category=${filter.category}&from=${filter.priceFrom}&to=${filter.priceTo}&rating=${filter.rating}`
      );
      return response.data;
    },
    [accessToken]
  );
  useEffect(() => {
    const name = searchParams.get("name") || "";
    const category = searchParams.get("category") || "";
    const priceFrom = searchParams.get("from") || "";
    const priceTo = searchParams.get("to") || "";
    const rating = searchParams.get("rating") || "";
    const page = searchParams.get("page") || "0";
    getBooksList({ name, category, priceFrom, priceTo, rating }, page)
      .then((data) => {
        console.log(data);
        setBooksList(data.content);
        setTotalPages(data.totalPages);
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
    window.scrollTo(0, 0);
    // if (name || category || priceFrom || priceTo || rating) {
    //   getFilteredBooks({ name, category, priceFrom, priceTo, rating }, page)
    //     .then((data) => {
    //       console.log(data);
    //       setBooksList(data.content);
    //       setPage(data.totalPages);
    //       //setTotalPages(data.totalPages);
    //     })
    //     .catch((error) => {
    //       if (isAxiosError(error)) {
    //         const data = error.response?.data;
    //         console.log("error:", data);
    //         //setMessage(data?.message);
    //       } else {
    //         //setMessage("Unknow error!!!");
    //         console.log(error);
    //       }
    //     });
    // } else {
    //   getBooksList(page)
    //     .then((data) => {
    //       console.log(data);
    //       setBooksList(data.content);
    //       setPage(data.totalPages);
    //       //setTotalPages(data.totalPages);
    //     })
    //     .catch((error) => {
    //       if (isAxiosError(error)) {
    //         const data = error.response?.data;
    //         console.log("error:", data?.message);
    //         //setMessage(data?.message);
    //       } else {
    //         //setMessage("Unknow error!!!");
    //         console.log(error);
    //       }
    //     });
    // }
    return () => {};
  }, [getBooksList, searchParams]);

  const onSearchSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (Object.keys(searchInfo).length === 0 || searchInfo.category === "")
      return setMessage("Please enter something to search");
    setMessage("");
    if (
      (searchInfo.priceFrom && searchInfo.priceFrom < 0) ||
      (searchInfo.priceTo && searchInfo.priceTo <= 0)
    ) {
      return setMessage("Price must be greater than 0");
    }
    if (
      searchInfo.priceFrom &&
      searchInfo.priceTo &&
      searchInfo.priceFrom > searchInfo.priceTo
    ) {
      return setMessage("Price From must be less than Price To");
    }

    if (searchInfo.name) {
      searchParams.set("name", searchInfo.name.replace(/\s+/g, " ").trim());
    } else {
      searchParams.delete("name");
    }
    if (searchInfo.category) {
      searchParams.set("category", searchInfo.category);
    } else {
      searchParams.delete("category");
    }
    if (searchInfo.priceFrom) {
      searchParams.set("from", searchInfo.priceFrom.toString());
    } else {
      searchParams.delete("from");
    }
    if (searchInfo.priceTo) {
      searchParams.set("to", searchInfo.priceTo.toString());
    } else {
      searchParams.delete("to");
    }
    if (searchInfo.rating) {
      searchParams.set("rating", searchInfo.rating.toString());
    } else {
      searchParams.delete("rating");
    }
    searchParams.set("page", "0");
    setSearchParams(searchParams);
    setCurrentPage(0);
    setSearchInfo({});
    setShowSearchModal(false);
  };

  const handleChangePage = async (
    event: React.ChangeEvent<unknown>,
    value: number
  ) => {
    setCurrentPage(value - 1);
    searchParams.set("page", (value - 1).toString());
    setSearchParams(searchParams);
  };

  return (
    <>
      <div className={`${style.header} mb-2`}>
        <h2> Books list</h2>
        <Button
          style={{ backgroundColor: "var(--primary-color)", color: "white" }}
          onClick={() => {
            setShowAddModal(true);
            console.log("add");
          }}
        >
          Add Book
        </Button>
      </div>
      <div className={`${style.content}`}>
        <div className="d-flex justify-content-between align-items-center">
          <h4>Result for {searchParams.get("books") || "all books"}</h4>
          <div
            id={style.search}
            className="px-3 py-2 d-flex justify-content-between align-items-center"
            onClick={() => setShowSearchModal(true)}
          >
            Search for books
            <BsSearch />
          </div>
        </div>
        <div>
          <Table className="mt-4" hover responsive="md">
            <thead className={`${style.tableHeader}`}>
              <tr>
                <th>ID</th>
                <th></th>
                <th>Name</th>
                {/* <th>Category</th> */}
                <th>Price</th>
                <th>Stock</th>
                <th>Sold</th>
                <th>Ratings</th>
                <th>Status</th>
                <th></th>
              </tr>
            </thead>
            <tbody className={`${style.tableBody}`}>
              {booksList.map((book) => (
                <BookItem key={book.id} book={book} />
              ))}
            </tbody>
          </Table>
        </div>
        <Pagination
          count={totalpage}
          page={currentPage + 1}
          showFirstButton
          showLastButton
          color="primary"
          style={{
            maxHeight: "25px",
            width: "75vw",
            // marginLeft: "auto",
            // marginRight: "auto",
            // height: "auto",
            // marginTop: "auto",
          }}
          onChange={handleChangePage}
        />
      </div>
      <AppModal
        showModal={showSearchModal}
        setShowModal={setShowSearchModal}
        title={"Search for books "}
      >
        <div>
          <form className={style.searchForm} onSubmit={onSearchSubmit}>
            <div>
              <label htmlFor="nameInput">Name or email</label>
              <input
                id="nameInput"
                name="nameInput"
                type="text"
                value={searchInfo.name || ""}
                onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                  setSearchInfo({
                    ...searchInfo,
                    name: e.target.value,
                  })
                }
              />
              <label htmlFor="categoryInput">Category</label>
              <Form.Select
                id="categoryInput"
                value={searchInfo.category || ""}
                onChange={(e) => {
                  setSearchInfo({
                    ...searchInfo,
                    category: e.target.value,
                  });
                }}
              >
                <option value="">Choose category</option>
                <option value="TECHNOLOGY">TECHNOLOGY</option>
                <option value="SCIENCE">SCIENCE</option>
                <option value="COMIC">COMIC</option>
                <option value="DETECTIVE">DETECTIVE</option>
                <option value="LITERARY">LITERARY</option>
                <option value="LIFESTYLE">LIFESTYLE</option>
                <option value="ROMANCE">ROMANCE</option>
                <option value="EDUCATION">EDUCATION</option>
              </Form.Select>

              <label htmlFor="priceInput">Price</label>
              <div className="d-flex align-items-center" id="priceInput">
                <input
                  id="fromInput"
                  name="fromInput"
                  type="number"
                  placeholder="From"
                  value={searchInfo.priceFrom || ""}
                  onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                    setSearchInfo({
                      ...searchInfo,
                      priceFrom: e.target.value,
                    })
                  }
                />
                <span>&nbsp;-&nbsp;</span>
                <input
                  id="toInput"
                  name="toInput"
                  type="number"
                  placeholder="To"
                  value={searchInfo.priceTo || ""}
                  onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                    setSearchInfo({
                      ...searchInfo,
                      priceTo: e.target.value,
                    })
                  }
                />
              </div>
              <label htmlFor="ratingInput">Ratings</label>
              <input
                id="ratingInput"
                name="ratingInput"
                type="number"
                step={1}
                min="0"
                max="5"
                value={searchInfo.rating || ""}
                onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                  setSearchInfo({
                    ...searchInfo,
                    rating: e.target.value,
                  })
                }
              />
              <div style={{ color: "red" }}>{message}</div>
            </div>
            <div className="d-flex justify-content-end">
              <Button
                className={style.cancelBtn}
                type="button"
                onClick={() => {
                  setSearchInfo({});
                  setMessage("");
                  setShowSearchModal(false);
                }}
              >
                Cancel
              </Button>
              <Button className={style.searchBtn} type="submit">
                Search
              </Button>
            </div>
          </form>
        </div>
      </AppModal>
      <AddBookModal showModal={showAddModal} setShowModal={setShowAddModal} />
    </>
  );
};

export default Books;
