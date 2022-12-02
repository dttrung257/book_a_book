import React, { useState, useLayoutEffect } from "react";
import styled from "styled-components";
import "./index.css";
import { FaStar, FaBookOpen } from "react-icons/fa";
import { Link } from "react-router-dom";
import * as bookSearch from "../../apis/book";
import BookCard from "../../components/Book/BookCard";
import { Subject, priceRanges, BookInfoBrief } from "../../models";
import ReactPaginate from "react-paginate";
import { useAppSelector } from "../../store/hook";
import { BookFilter } from "../../models/Filter";
import { isAxiosError } from "../../apis/axiosInstance";
const Wrapper = styled.div`
  background-color: #ffffff;
  position: relative;
  overflow: auto;
  min-height: 100vh;
  height: fit-content;
  display: flex;
  flex-direction: column;
`;

const price = [0, 5, 10, 25, 50, 100000000];

const CategoryPage = () => {
  const [searchResult, setSearchResult] = useState<BookInfoBrief[]>([]);
  const [page, setPage] = useState<number>(0);
  const [hover, setHover] = useState(-1);
  const [filter, setFilter] = useState<BookFilter>({
    page: 0,
    category: "",
    rating: 0,
    from: 0.1,
    to: 100000000,
    size: 12,
  });
  const stars = Array(5).fill(0);

  const name = useAppSelector((state) => state.search.name);

  const handleHoverStar = (value: number) => {
    setHover(value);
  };
  const handlePageClick = (value: number) => {
    setFilter({
      ...filter,
      page: value,
    });
  };

  const handlePriceChange = (id: number) => {
    if (filter.from !== price[id]) {
      setFilter({
        ...filter,
        from: price[id],
        to: price[id + 1],
        page: 0,
      });
    } else {
      setFilter({
        ...filter,
        from: 0.1,
        to: 100000000,
        page: 0,
      });
    }
  };
  const handleCategoryClick = (category: string) => {
    if (filter.category === category) {
      setFilter({
        ...filter,
        category: "",
        page: 0,
      });
    } else {
      setFilter({
        ...filter,
        category: category,
        page: 0,
      });
    }
  };
  const handleRatingClick = (value: number) => {
    if (filter.rating === value) {
      setFilter({
        ...filter,
        rating: 0,
        page: 0,
      });
    } else {
      setFilter({
        ...filter,
        rating: value,
        page: 0,
      });
    }
  };

  useLayoutEffect(() => {
    const fetchApi = async () => {
      try {
        const result = await bookSearch.getBookByFilter(filter, name);
        setSearchResult(result.content);
        setPage(result.totalPages);
        console.log(result);
      } catch (error) {
        if (isAxiosError(error)) {
          console.log(error);
        }
      }
    };
    fetchApi();
  }, [name, filter]);
  console.log(searchResult);
  return (
    <Wrapper>
      <div className="container">
        <div className="filter">
          <div className="filterArea">
            <div className="filterTitle">
              <span>CATEGORY</span>
            </div>
            {Subject.map((subject, index) => (
              <div
                key={index}
                onClick={() => handleCategoryClick(subject)}
                className={
                  subject === filter.category
                    ? "TitleOnClick"
                    : "criterionTitle"
                }
              >
                {subject}
              </div>
            ))}
          </div>
          <div className="filterArea">
            <div className="filterTitle">
              <span>PRICES</span>
            </div>
            {priceRanges.map((priceRange, index) => (
              <div
                className={
                  price[index] === filter.from
                    ? "TitleOnClick"
                    : "criterionTitle"
                }
                key={index}
                onClick={() => handlePriceChange(index)}
              >
                {priceRange}
              </div>
            ))}
          </div>
          <div className="filterArea">
            <div className="filterTitle">
              <span>review</span>
            </div>
            <div className="starContainer">
              {stars.map((_, index) => {
                return (
                  <FaStar
                    key={index}
                    className="starItem"
                    size={24}
                    style={
                      filter.rating > index || index <= hover
                        ? {
                            color: "#dcd13a",
                          }
                        : { color: "#989898" }
                    }
                    onClick={() => handleRatingClick(index + 1)}
                    onMouseOver={() => handleHoverStar(index)}
                    onMouseLeave={() => handleHoverStar(-1)}
                  />
                );
              })}
            </div>
          </div>
        </div>
        <div className="content">
          <div className="header">
            <h2>
              <span>
                {filter.category === "" ? "The Book Store" : filter.category}
              </span>
            </h2>
          </div>
          <div className="headBanner">
            <FaBookOpen size={14} className="mx-2" />
            Have a good day at Book a book. Get it at our home page
            <Link to={"#"} className="mx-2">
              Home
            </Link>
          </div>
          <div className="bookContainer">
            {searchResult.map((result) => {
              return <BookCard key={result.id} book={result} />;
            })}
          </div>
          <ReactPaginate
            pageCount={page}
            nextLabel={">>"}
            previousLabel={"<<"}
            breakLabel={" . . . "}
            marginPagesDisplayed={1}
            pageRangeDisplayed={2}
            containerClassName={page <= 1 ? "pageNull" : "pageContainer"}
            pageClassName="pageItem"
            previousClassName="pageItem"
            nextClassName="pageItem"
            onPageChange={(data) => handlePageClick(data.selected)}
            activeClassName="currentPage"
            forcePage={filter.page}
          />
        </div>
      </div>
    </Wrapper>
  );
};
export default CategoryPage;
