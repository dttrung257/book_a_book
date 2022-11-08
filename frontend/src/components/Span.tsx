import { ReactElement } from "react";
import { IconType } from "react-icons";

interface SpanProps {
  icon: ReactElement;
  text: string;
  rectLeftWidth: number;
  rectRightWidth?: number;
  rectText?: string;
}

const Span = (span: SpanProps) => {
  return (
    <div>
      <svg width="100%" height="60">
        {span.rectRightWidth !== undefined ? (
          <svg>
            <rect
              x={50 + span.rectLeftWidth}
              y="10"
              rx="5"
              ry="5"
              width={span.rectRightWidth}
              height="35"
              style={{ fill: "#fff", stroke: "#666", strokeWidth: 0.5 }}
            />
            <text
              x={span.rectRightWidth + span.rectLeftWidth}
              y="35"
              fill="#008b8b"
              fontSize={24}
              fontFamily="system-ui"
              fontWeight={500}
            >
              {span.rectText}
            </text>
          </svg>
        ) : (
          <></>
        )}
        <rect
          x="60"
          y="10"
          rx="5"
          ry="5"
          width={span.rectLeftWidth}
          height="35"
          style={{ fill: "#008B8B" }}
        >
          {span.icon}
          <span> {span.text}</span>
        </rect>
        <polygon points="157,10 157,45 200,27.5" style={{ fill: "#008B8B" }} />
        <text
          x={70}
          y="35"
          fill="#fff"
          fontSize={24}
          fontFamily="system-ui"
          fontWeight={600}
        >
          {span.text}
        </text>
        <div>{span.icon}</div>
      </svg>
    </div>
  );
};

export default Span;
