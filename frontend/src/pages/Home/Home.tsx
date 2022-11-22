import { useEffect, useState } from "react";
import { AiFillThunderbolt } from "react-icons/ai";
import { Navigate, useNavigate } from "react-router-dom";
import Slide from "../../components/SlideShow";
import Span from "../../components/Span";
import "./index.css";

import { authActions } from "../../store/authSlice";
import { useAppDispatch, useAppSelector } from "../../store/hook";
import { getBestSeller, getBooksOfCategory } from "../../apis/book";
import { BookInfoBrief } from "../../models";
import BookCarousel from "../../components/BookCarousel/BookCarousel";
import { MdCollectionsBookmark } from "react-icons/md";
import { GiBookCover } from "react-icons/gi";
import Collections from "../../components/Collections/Collections";
import Privacy from "../../components/Privacy/Privacy";
import { FaBook } from "react-icons/fa";

const Home = () => {
  const isLoggedIn = useAppSelector((state) => state.auth.isLoggedIn);

  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const { user, accessToken } = useAppSelector((state) => state.auth);

  const onLogout = () => {
    dispatch(authActions.logout());
  };
  const [bestSeller, setBestSeller] = useState<BookInfoBrief[]>([]);
  const [education, seteducation] = useState<BookInfoBrief[]>([]);
  const [detective, setdetective] = useState<BookInfoBrief[]>([]);
  const [comic, setcomic] = useState<BookInfoBrief[]>([]);
  useEffect(() => {
    getBestSeller({ size: 10 })
      .then((res) => {
        setBestSeller(res.content as BookInfoBrief[]);
        console.log(bestSeller);
      })
      .catch((err) => {
        console.log(err);
      });
    getBooksOfCategory({ category: "lifestyle", size: 10 })
      .then((res) => {
        seteducation(res.content as BookInfoBrief[]);
        console.log(education);
      })
      .catch((err) => {
        console.log(err);
      });
    getBooksOfCategory({ category: "detective", size: 10 })
      .then((res) => {
        setdetective(res.content as BookInfoBrief[]);
        console.log(detective);
      })
      .catch((err) => {
        console.log(err);
      });
    getBooksOfCategory({ category: "comic", size: 10 })
      .then((res) => {
        setcomic(res.content as BookInfoBrief[]);
        console.log(comic);
      })
      .catch((err) => {
        console.log(err);
      });

      // Promise.all([getBestSeller({ size: 10 }), getBooksOfCategory({ category: "lifestyle", size: 10 }), getBooksOfCategory({ category: "detective", size: 10 }), getBooksOfCategory({ category: "comic", size: 10 })]).then(([bestRes, lifeRes, detectRes, comicRes])=>{
      //   setBestSeller(bestRes.content as BookInfoBrief[]);
      //   seteducation(lifeRes.content as BookInfoBrief[]);
      //   setdetective(detectRes.content as BookInfoBrief[]);
      //   setcomic(comicRes.content as BookInfoBrief[]);
      // }).catch(error => {
      //   console.log(error);
      // })
  }, []);

  if (!isLoggedIn) return <Navigate to="/login" />;

  return (
    <div>
      <Slide />
      <div id="content">
        <Span
          icon={<AiFillThunderbolt color="fff" fontSize={24} />}
          text="Best Seller"
          rectLeftWidth={140}
          rectRightWidth={window.screen.width - 60 - 60}
          rectText="All"
        />
        {bestSeller.length>0&&<div id="books">
          <BookCarousel books={bestSeller} />
        </div>}
        <img
          className="fit"
          src="https://live.staticflickr.com/65535/52501278981_cf9503fea1_h.jpg"
          alt="img"
        />
        <Span
          icon={<MdCollectionsBookmark color="fff" fontSize={24} />}
          text="Collections"
          rectLeftWidth={150}
        />
        <Collections />
        <img
          className="fit"
          src="https://live.staticflickr.com/65535/52501447434_26eeab198f_h.jpg"
          alt="img"
        />
        <Span
          icon={<FaBook color="fff" fontSize={24} />}
          text="Comic"
          rectLeftWidth={100}
          rectRightWidth={window.screen.width - 60 - 60}
          rectText="All"
        />
        {comic.length>0&&<div id="books">
          <BookCarousel books={comic} />
        </div>}
        <img
          className="fit"
          src="https://live.staticflickr.com/65535/52501174531_5d0dc68331_h.jpg"
          alt="img"
        />
        <Span
          icon={<FaBook color="fff" fontSize={24} />}
          text="Education"
          rectLeftWidth={140}
          rectRightWidth={window.screen.width - 60 - 60}
          rectText="All"
        />
        {education.length>0&&<div id="books">
          <BookCarousel books={education} />
        </div>}
        <Span
          icon={<GiBookCover color="fff" fontSize={24} />}
          text="Book of the week"
          rectLeftWidth={210}
        />
        <div id="bookOfWeekCont">
          <p>
            <img src="https://live.staticflickr.com/65535/52447753485_f125a528bd_n.jpg" alt="img" />
            <span> Le Chuchoteur - Donato Carrisi</span>
            <br />
            Five little girls have disappeared.
            <br /> Five small pits were dug in the clearing.
            <br /> At the bottom of each, a small arm, the left. Since
            investigating the abductions of young girls, criminologist Goran
            Gavila and his team of special agents have the impression of being
            manipulated. Each macabre discovery, each clue leads them to
            different assassins. The discovery of a sixth arm, in the clearing,
            belonging to an unknown victim, convinces them to call Mila Vasquez,
            an expert in kidnapping cases, for reinforcement. Behind closed
            doors in a Spartan apartment converted into a headquarters, Gavila
            and his agents are going to construct a theory that no one wants to
            believe: all the murders are linked, the real culprit is elsewhere.
            When children are killed, God is silent, and the devil whispers...
          </p>
        </div>
        <Span
          icon={<FaBook color="fff" fontSize={24} />}
          text="Detective"
          rectLeftWidth={140}
          rectRightWidth={window.screen.width - 60 - 60}
          rectText="All"
        />
        {detective.length>0&&<div id="books">
          <BookCarousel books={detective} />
        </div>}

        <Privacy />
      </div>
      {/* <h1>Home</h1>
			<div>
				<ul>
					<li>{user.firstName}</li>
					<li>{user.lastName}</li>
					<li>{JSON.stringify(user.role)}</li>
					<li>{accessToken}</li>
				</ul>
			</div>
			<button onClick={() => navigate("/test")}>click to test</button>
			<button onClick={onLogout}>Logout</button> */}
    </div>
  );
};

export default Home;
