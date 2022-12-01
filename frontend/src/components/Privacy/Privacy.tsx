import { FcAssistant, FcShipped, FcSynchronize } from "react-icons/fc";
import "./index.css";

const Privacy = () => {
  return (
    <div>
      <div id="privacyFooter">
        <div className="privacyFooter">
          <span className="iconPr">
            <FcShipped fontSize={60} />
          </span>
          <p>
            Free shipping
            <br />
            <span>Bills over 50$</span>
          </p>
        </div>
        <div
          className="privacyFooter"
          style={{
            borderLeft: "1px solid #f0f0f0",
            borderRight: "1px solid #f0f0f0",
          }}
        >
          <span className="iconPr">
            <FcSynchronize fontSize={60} />
          </span>
          <p>
            Free returns
            <br />
            <span>Producer's fault</span>
          </p>
        </div>
        <div className="privacyFooter">
          <span className="iconPr">
            <FcAssistant fontSize={60} />
          </span>
          <p>
            Live chat
            <br />
            <span>Support 24/7</span>
          </p>
        </div>
      </div>
      <div id="sponsor">
        <a href="https://nxbvanhoc.com.vn/" target="_blank" rel="noreferrer">
          <img
            alt="sponsor"
            src="https://live.staticflickr.com/65535/52501507909_2781d3dab2_m.jpg"
          />
        </a>
        <a href="https://www.nxbtre.com.vn/" target="_blank" rel="noreferrer">
          <img
            alt="sponsor"
            src="https://live.staticflickr.com/65535/52501781958_115a57b5e4_m.jpg"
          />
        </a>
        <a href="https://nxbgd.vn/" target="_blank" rel="noreferrer">
          <img
            alt="sponsor"
            src="https://live.staticflickr.com/65535/52501262211_9f9f9946eb_m.jpg"
          />
        </a>
        <a href="https://nxbkimdong.com.vn/" target="_blank" rel="noreferrer">
          <img
            alt="sponsor"
            src="https://live.staticflickr.com/65535/52501507489_686a077830_m.jpg"
          />
        </a>
        <a href="https://nhanam.vn/" target="_blank" rel="noreferrer">
          <img
            alt="sponsor"
            src="https://live.staticflickr.com/65535/52501781378_e9bfc4b950_m.jpg"
          />
        </a>
      </div>
    </div>
  );
};

export default Privacy;
