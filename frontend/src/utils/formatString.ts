export const formatStr = (s: string, n: number) => {
  if (s.length < n) return s;
  else {
    return s.slice(0, n - 2).concat("...");
  }
};
