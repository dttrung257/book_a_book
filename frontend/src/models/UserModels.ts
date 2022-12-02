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
  address?: string;
  phoneNumber?: string;
  avatar?: string;
  acceptPrivacy?: string;
}

export interface PassError {
  oldPassword?: string;
  cfPassword?: string;
  newPassword?: string;
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

export interface UserDetail {
  firstName: string;
  lastName: string;
  email: string;
  gender: string;
  phoneNumber: string;
  address: string;
  avatar: string;
}

export interface changePass {
  oldPassword: string;
  newPassword: string;
  cfPassword: string;
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
