
import Footer from "../../components/Footer/Footer";
import Header from "../../components/Header/Header";
import "./index.css";

const AboutUs = () => {
  return (
    <div>
      	<p id="BookABook">
			<span><i>Book</i></span>
			a<i>Book</i>	
        </p>
        
        <p className="about-us para">	
        	Welcome to BookABook, the place to find the best books for every taste and occasion. We thoroughly check the quality of our goods, working only with reliable suppliers so that you only receive the best quality product.
        </p>
        
        <p id="Commitments" className="about-us">
        	Our commitments
        </p>
        
        <p className="about-us para">	
        	We at BookABook believe in high quality and exceptional customer service. But most importantly, we believe shopping is a right, not a luxury, so we strive to deliver the best products at the most affordable prices, and ship them to you regardless of where you are located.
        </p>
    </div>
  );
};
export default AboutUs;
