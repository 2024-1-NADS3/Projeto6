import Partner from "../models/Partner.js";

const createPartner = async (req, res) => {
  const { instituitionName, cnpj, email, cellnumber, password } = req.body;

  const partner = new Partner(
    instituitionName,
    cnpj,
    email,
    cellnumber,
    password,
  );

  const partnerExist = await Partner.findByEmail(email);

  console.log(partnerExist);

  if (partnerExist) {
    return res.status(409).json({ message: "Email já cadastrado" });
  }

  const partnerCreated = await partner.create();

  if (!partnerCreated) {
    return res
      .status(500)
      .json({ message: "Não foi possível cadastrar o parceiro" });
  }

  console.log("parceiro criado com sucesso:", partnerCreated);

  res.status(200).json({ message: "parceiro cadastrado com sucesso" });
};

export const partnerController = {
  createPartner,
};
