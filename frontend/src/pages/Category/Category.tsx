import React, { useEffect, useState } from "react"
import styled from "styled-components";
import style from "./Category.module.css";
import { FaStar,FaChevronDown, FaBookOpen} from "react-icons/fa";
import bookStyle from "./Book.module.css";
import Footer from "../../components/Footer/Footer"
import Header from "../../components/Header/Header"
import { Link } from "react-router-dom";
import * as bookSearch from "../../apis/book";
const Wrapper = styled.div`
	background-color: #ffffff;
	position: relative;
	overflow: auto;
	min-height: 100vh;
`;

const subjects = ['Literary','Lifestyle','Education','Technology','Science',
                    'Comic','Romance','Business','Detective'];
const priceRanges = ['Under $5','$5 - $10','$10 - $25','$25 - $50','Over $50'];
const price = [0, 5, 10, 25, 50];
const LOWHIGH = "Price - Low to High";
const BESTSELL = "Best Sellers";
const HIGHLOW = "Price - High to Low";
const Category = () => {
    const [type, setType] = useState("");
    const [minPrice,setMinPrice] = useState(0);
    const [maxPrice,setMaxPrice] = useState(1000);
    const [starValue,setStarValue] = useState(0);
    const [sortType, setSortType] = useState(BESTSELL);
    const [searchResult,setSearchResult] = useState([]);
    const stars = Array(5).fill(0);

    const handleStarClick = (value:number) =>{
        setStarValue(value);

    }
    const handleSortClick = (sort:string) => {
        setSortType(sort);
    }

    const handleTypeClick = (category:string) => {
        setType(category);
        console.log(type);
    }

    const handlePriceClick = (id: number) => {
        setMinPrice(price[id]);
        setMaxPrice(price[id+1]);
    }
    useEffect(() =>{
        const fetchApi = async () => {
            const result = await bookSearch.getAllBook();
            setSearchResult(result);
        }
        fetchApi();

    },[type])
    console.log(searchResult);
    return (
        <Wrapper>
            <Header />
            <div className={`${style.container}`}>
                <div className={`${style.filter}`}>
                <div className={`${style.filterField}`}>
                        <div className={style.filterTitle}>CATEGORY</div>
                        {subjects.map((subject,index)=>(
                            <div
                                key={index}
                                onClick = {() =>handleTypeClick(subject)}
                                className={style.criterionTitle}
                            >
                                {subject}
                            </div>
                        ))}
                    </div>
                    <div className={`${style.filterField}`}>
                    <div className={style.filterTitle}>PRICES</div>
                        {priceRanges.map((priceRange,index) => (
                                <div className={style.criterionTitle} key={index}
                                    onClick = {()=>handlePriceClick(index)}
                                    >
                                    {priceRange}
                                </div>
                            ))}        
                    </div>
                    <div className={`${style.filterField}`}>
                    <div className={style.filterTitle}>review</div>
                    <div className={`${style.starContainer} `}>
                            {stars.map((_,index)=>{
                                return (
                                    <FaStar 
                                    key={index}
                                    size={24}
                                    style={starValue>index?{
                                        color:'#faeb07',
                                    } :{color:'#989898'}}
                                    onClick={() =>handleStarClick(index+1)}
                                    className={`${style.starItem}`}
                                    />
                                )
                            })
                            }
                    </div>
                    </div>                    
                </div>
                <div className={`${style.bookContainer}`}>
                    <div className={`${bookStyle.header}`}>
                            <div className={`${bookStyle.mainSubject}`}>
                                { type === "" ? "The Book Store": type}
                            </div>
                            <label className={`${bookStyle.dropDown}`} >
                                {sortType}
                            <FaChevronDown className={`${bookStyle.dropIcon}`}/>
                            <ul className={`${bookStyle.dropDownList}`} >
                                <li className={`${bookStyle.dropDownItem}`} onClick = {() =>handleSortClick(BESTSELL)}>Best Sellers</li>
                                <li className={`${bookStyle.dropDownItem}`} onClick = {() =>handleSortClick(LOWHIGH)}>Price - Low to High</li>
                                <li className={`${bookStyle.dropDownItem}`} onClick = {() =>handleSortClick(HIGHLOW)}>Price - High to Low</li>
                            </ul>
                            </label>
                    </div>

                    <div className={`${bookStyle.headBanner}`}>
                        <FaBookOpen size={14} className='mx-2' />
                        Have a good day at Book a book. Get it at our home page   
                        <Link to={"#"} className='mx-2'>Home</Link>
                    </div>
            </div>
            </div>
            <Footer />
        </Wrapper>
    )
};
export default Category;