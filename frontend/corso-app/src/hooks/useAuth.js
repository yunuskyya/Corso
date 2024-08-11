import { useAppSelector } from "../redux/hooks";

const useAuth = () => {
    const user = useAppSelector((state) => state.auth.user);
    const fetch_status = useAppSelector((state) => state.auth.fetch_status);
    const status = useAppSelector((state) => state.auth.status);

    console.log("user: ", user);

    console.log("authority", user ? user.role : "no user");
    return {
        user,
        status,
        fetch_status,
        isAuthenticated: user !== null && user !== undefined,
        role: user?.role,
    };
};

export default useAuth;
