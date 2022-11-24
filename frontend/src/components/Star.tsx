import { FaStar } from "react-icons/fa";
import { renderToStaticMarkup } from "react-dom/server";
import { createElement } from "react";
import styled from "styled-components";

const reactSvgComponentToMarkupString = (Component: any, props: any) =>
  `data:image/svg+xml,${encodeURIComponent(
    renderToStaticMarkup(createElement(Component, props))
  )}`;

  const FullStar = styled.span`
  margin: 0 5px;
  &:after {
    content: ${() =>
      `url(${reactSvgComponentToMarkupString(FaStar, {
        color: "#fed221"
      })})`};
  }
`;

const Empty = styled.span`
margin: 0 5px;
  &:after {
    content: ${() =>
      `url(${reactSvgComponentToMarkupString(FaStar, {
        color: "#e7e7e7"
      })})`};
  }
`;

const HalfStar = styled.span`
&:after {
  content: ${() =>
    `url(${reactSvgComponentToMarkupString(FaStar, {
      color: "#e7e7e7"
    })})`};
    width: 50%;
    left: .5rem;
    text-indent: -.5rem;
    position: absolute;
    overflow: hidden;
}
&:before {
  content: ${() =>
    `url(${reactSvgComponentToMarkupString(FaStar, {
      color: "fed221"
    })})`};
  width: 50%;
}
`;

const Star = (props: {rate: number | undefined}) => {
  //full: 2, half: 1, fullLeft: 0
  let listStar = [0,0,0,0,0]
  if (props.rate !== undefined) {
      let numOfFull = Math.floor(props.rate)
      let half = 0;
      let tmp = props.rate - numOfFull;
      if (tmp > 0.8) numOfFull++
      else if (tmp > 0.25) half = 1
      let i = 0;
      while(numOfFull > 0) {
        listStar[i] = 2;
        i++
        numOfFull--;
      }
      if (half > 0) listStar[i] = 1
  }

  return (
    <div style={{display: "inline-block"}}>
    {listStar.map((star, i) => {
      if (star === 0) return <Empty key={i}/>
      else if (star === 1) return <div key={i} style={{position: "relative", display: "inline-block", margin: "0 5px"}}> <HalfStar/></div>
      return <FullStar key={i} />
    })}      
      </div>
      );
}

export default Star;