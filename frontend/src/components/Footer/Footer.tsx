import { Button } from "@mui/material";
import { FaFacebook, FaWhatsapp, FaTwitter, FaInstagram } from "react-icons/fa";
import { FaPhoneAlt, FaBuilding } from "react-icons/fa";
import { MdMail } from "react-icons/md";
import "./index.css";

const Footer = () => {
  return (
    <div id="footer">
      <div id="container">
        <div id="support">
          <div id="privacy">
            <h3>PRIVACY</h3>
            <p>Security privacy</p>
            <p>Insurance privacy</p>
            <p>Terms of service</p>
          </div>
          <div id="help">
            <h3>HELP & SUPPORT</h3>
            <p>Shipping info</p>
            <p>Returns</p>
            <p>How to order</p>
            <p>How to track</p>
          </div>
        </div>
        <div id="findUs">
          <h3>FIND US ON</h3>
          <span>
            <a href="https://m.me/thuy.quynhz.169">
              <FaFacebook color="4267B2" fontSize={30} />
            </a>
          </span>
          <span>
            <a href="https://m.me/thuy.quynhz.169">
              <FaWhatsapp color="25D366" fontSize={30} />
            </a>
          </span>
          <span>
            <a href="https://m.me/thuy.quynhz.169">
              <FaInstagram color="FCAF45" fontSize={30} />
            </a>
          </span>
          <span>
            <a href="https://m.me/thuy.quynhz.169">
              <FaTwitter color="1DA1F2" fontSize={30} />
            </a>
          </span>
          <h3 style={{ marginTop: "20px" }}>SIGN UP FOR NEWS</h3>
          {/* <TextField
            id="outlined-basic"
            variant="outlined"
            size="small"
            //   value={searchKey}
            //   onChange={onChangeSearchBox}
            placeholder="Your email address..."
            fullWidth={true}
            InputProps={{
              endAdornment: (
                <InputAdornment position="end" style={{ marginRight: "-3.5%" }}>
                  <Button
                    variant="contained"
                    color="primary"
                    style={{ height: "44px", marginBottom: "5px" }}
                  >
                    Subcribe
                  </Button>
                </InputAdornment>
              ),
            }}
          /> */}
          <div style={{ position: "relative" }}>
            <input id="searchBar" placeholder="Your email address..." />
            <Button variant="contained" color="primary" id="subscribe">
              Subcribe
            </Button>
          </div>
        </div>
        <div id="intro" style={{ marginRight: "5%" }}>
          <p id="logo">
            <span>
              <i>Book</i>
            </span>
            a<i>Book</i> - Revs Your Heart
          </p>
          <svg height="10" width="50">
            <line
              x1="0"
              y1="0"
              x2="50"
              y2="0"
              style={{ stroke: "#008B8B", strokeWidth: 8 }}
            />
          </svg>
          <h3>Contact us:</h3>

          <p>
            <FaPhoneAlt color="008B8B" /> 19000091 - 0333.444.555
          </p>
          <p>
            <FaBuilding color="008B8B" /> Tầng 3, Tòa Hihi, Phố HeHe, Haha,
            Hoho, Việt Nam
          </p>
          <p>
            <MdMail color="008B8B" /> bookabook@gmail.com
          </p>
        </div>
      </div>
      <div id="copyright">
        <p>@2022- Book a book All Rights Reserved</p>
      </div>
    </div>
  );
};

export default Footer;
