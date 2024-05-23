import jwt from "jsonwebtoken";

/** Middleware para verificar se o token JWT enviado pelo usuário/parceiro é válido */
export const checkJwt = (req, res, next) => {
 try {
    const token = req.headers.authorization.split(' ')[1];
    const decodedToken = jwt.verify(token, process.env.JWT_SECRET);
    
    const userId = decodedToken.userId;
    const userType = decodedToken.userType;

    req.userId = userId;
    req.userType = userType;

    next();
 } catch (error) {
    res.status(401).json({
      message: 'Token inválido ou expirado'
    });
 }
};