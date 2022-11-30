export interface UserSignIn {
  email: string;
  password: string;
}

export interface VerifyEmail {
  email: string;
  verifyCode: string;
}

export interface UserSignUp {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  gender: string;
}

export interface AuthInfo {
  firstName: string;
  lastName: string;
  email: string;
  address?: string;
  password: string;
  confirmPassword: string;
  gender: string;
  acceptPrivacy: boolean;
}

export interface AuthError {
  firstName?: string;
  lastName?: string;
  email?: string;
  password?: string;
  confirmPassword?: string;
  gender?: string;
  acceptPrivacy?: string;
}

export interface UserInfo {
  avatar?: string;
  firstName: string;
  lastName: string;
  authority: string;
}

export interface LoginInfo {
  accessToken: string;
  user: UserInfo;
}

export interface UserDetailInfo extends UserInfo {
  id: string;
  email: string;
  gender: string;
  phoneNumber?: number;
  address?: string;
  locked: boolean;
  createdAt: string;
  emailVerified: boolean;
}
