import React, { useState, useLayoutEffect } from "react";
import styled from "styled-components";
import style from "./Category.module.css";
import { FaStar, FaChevronDown, FaBookOpen } from "react-icons/fa";
import bookStyle from "./Book.module.css";
import Footer from "../../components/Footer/Footer";
import Header from "../../components/Header/Header";
import { Link } from "react-router-dom";
import * as bookSearch from "../../apis/book";
import BookCard from "../../components/Book/BookCard";
import { BookInfo,Subject, priceRanges,SortType} from "../../models";
import ReactPaginate from "react-paginate";
import { useAppSelector} from "../../store/hook";
import { BookFilter } from "../../models/Filter";
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
  const [sortType, setSortType] = useState(SortType.LOWTOHIGH);
  const [searchResult, setSearchResult] = useState<BookInfo[]>([]);
  const [page,setPage] = useState<number>(0);
  const [filter, setFilter] = useState<BookFilter>({
    page: 0,
    category:"",
    rating: 0,
    from: 0,
    to: 1000000,
    size: 12,
  });
  const stars = Array(5).fill(0);

  const name = useAppSelector((state) => state.search.name);

  const handleSortClick = (sort:SortType) => {
    setSortType(sort);
  };

  const handlePageClick = (data:any) => {
    setFilter({
      ...filter,
      page:data.selected,
    })
  };

  const handlePriceChange = (id: number) => {
    setFilter({
      ...filter,
      from: price[id],
      to: price[id+1],
    });
  }
  const handleCategoryClick = (category:string) =>{
    setFilter({
      ...filter,
      category:category,
    });
  };
  const handleRatingClick = (value:number) => {
    setFilter({
      ...filter,
      rating: value,
    });
  }

  useLayoutEffect(() => {
    const fetchApi = async () => {
      try {
        const result = await bookSearch.getBookByFilter(filter,name);
        setSearchResult(result.content);
        setPage(result.totalPages);
        console.log(name);
      } catch (error) {
        console.log(error);
      }
    };
    fetchApi();
  }, [name,filter]);
  console.log(searchResult);
  return (
    <Wrapper>
      <Header />
      <div className={`${style.container}`}>
        <div className={`${style.filter}`}>
          <div className={`${style.filterField}`}>
            <div className={style.filterTitle}>CATEGORY</div>
            {Subject.map((subject, index) => (
              <div
                key={index}
                onClick={() => handleCategoryClick(subject)}
                className={style.criterionTitle}
              >
                {subject}
              </div>
            ))}

          </div>
          <div className={`${style.filterField}`}>
            <div className={style.filterTitle}>PRICES</div>
            {priceRanges.map((priceRange, index) => (
              <div
                className={style.criterionTitle}
                key={index}
                onClick={() => handlePriceChange(index)}
              >
                {priceRange}
              </div>
            ))}
          </div>
          <div className={`${style.filterField}`}>
            <div className={style.filterTitle}>review</div>
            <div className={`${style.starContainer} `}>
              {stars.map((_, index) => {
                return (
                  <FaStar
                    key={index}
                    size={24}
                    style={
                      filter.rating > index
                        ? {
                            color: "#dcd13a",
                          }
                        : { color: "#989898" }
                    }
                    onClick={() => handleRatingClick(index + 1)}
                    className={`${style.starItem}`}
                  />
                );
              })}
            </div>
          </div>
        </div>
        <div className={`${style.content}`}>
          <div className={`${bookStyle.header}`}>
            <div className={`${bookStyle.mainSubject}`}>
              {filter.category === "" ? "The Book Store" : filter.category}
            </div>
            <label className={`${bookStyle.dropDown}`}>
              {sortType}
              <FaChevronDown className={`${bookStyle.dropIcon}`} />
              <ul className={`${bookStyle.dropDownList}`}>
                <li
                  className={`${bookStyle.dropDownItem}`}
                  onClick={() => handleSortClick(SortType.LOWTOHIGH)}
                >
                  {SortType.LOWTOHIGH}
                </li>
                <li
                  className={`${bookStyle.dropDownItem}`}
                  onClick={() => handleSortClick(SortType.HIGHTOLOW)}
                >
                  {SortType.HIGHTOLOW}
                </li>
                <li
                  className={`${bookStyle.dropDownItem}`}
                  onClick={() => handleSortClick(SortType.ALPHABET)}
                >
                  {SortType.ALPHABET}
                </li>
                <li
                  className={`${bookStyle.dropDownItem}`}
                  onClick={() => handleSortClick(SortType.ALPHABETREVERT)}
                >
                  {SortType.ALPHABETREVERT}
                </li>
              </ul>
            </label>
          </div>

          <div className={`${bookStyle.headBanner}`}>
            <FaBookOpen size={14} className="mx-2" />
            Have a good day at Book a book. Get it at our home page
            <Link to={"#"} className="mx-2">
              Home
            </Link>
          </div>
          <div className={`${style.bookContainer}`}>
            {searchResult.map((result) => {
              return (
                <BookCard
                  key={result.id}
                  id={result.id}
                  name={result.name}
                  image={result.image}
                  sellingPrice={result.sellingPrice}
                  rating={result.rating}
                />
              );
            })}
          </div>
          <ReactPaginate 
          pageCount={page}
          nextLabel={'next'}
          previousLabel={'previous'}
          breakLabel={' . . . '}
          marginPagesDisplayed={2}
          pageRangeDisplayed={3}
          containerClassName={`${style.pageContainer}`}
          pageClassName={`${style.pageItem}`}
          previousClassName={`${style.pageItem}`}
          nextClassName={`${style.pageItem}`}
          onPageChange={handlePageClick}
          activeClassName={`${style.currentPage}`}
          />
        </div>
      </div>
      <Footer />
      
    </Wrapper>
  );
};
export default CategoryPage;
