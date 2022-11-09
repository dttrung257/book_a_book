import { ReactElement } from "react";

export interface SpanProps {
  icon: ReactElement;
  text: string;
  rectLeftWidth: number;
  rectRightWidth?: number;
  rectText?: string;
}
