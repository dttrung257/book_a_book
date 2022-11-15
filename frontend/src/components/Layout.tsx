import { Outlet } from "react-router-dom";
import Contact from "./Contact/Contact";
import Footer from "./Footer/Footer";
import Header from "./Header/Header";

const Layout = () => {
  return (
    <>
      <Header />
      <Outlet />
      <Contact />
      <Footer />
    </>
  );
};

export default Layout;
