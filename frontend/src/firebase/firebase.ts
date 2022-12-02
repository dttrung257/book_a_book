import "firebase/storage";
import { initializeApp } from "firebase/app";
import { getAnalytics } from "firebase/analytics";
import { getFirestore } from "firebase/firestore";
import { getStorage } from "firebase/storage";

const firebaseConfig = {
  apiKey: "AIzaSyCgKdXphisIHH1k8-u3JyTxm1bq1RJzuo0",
  authDomain: "bookabook-29df7.firebaseapp.com",
  projectId: "bookabook-29df7",
  storageBucket: "bookabook-29df7.appspot.com",
  messagingSenderId: "364118182657",
  appId: "1:364118182657:web:812342fdb4b90faa51a900",
  measurementId: "G-JE882KCNN8",
};

const app = initializeApp(firebaseConfig);
const analytics = getAnalytics(app);
const db = getFirestore(app);
const storage = getStorage(app);

export { db, storage, app, analytics };
