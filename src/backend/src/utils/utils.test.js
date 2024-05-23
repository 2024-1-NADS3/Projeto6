/** Testes para a função validadeCNPJ */
import { utils } from "./utils";

describe('validateCNPJ', () => {
  test('valid CNPJ returns true', () => {
    expect(utils.validateCNPJ('57256634000127')).toBe(true);
  });

  test('invalid CNPJ returns false', () => {
    expect(utils.validateCNPJ('12345678901234')).toBe(false);
  });

  test('invalid CNPJ with equals numbers returns false', () => {
    expect(utils.validateCNPJ('55555555555555')).toBe(false);
  });

  test('invalid CNPJ with less numbers returns false', () => {
    expect(utils.validateCNPJ('12123123123')).toBe(false);
  });

  test('invalid CNPJ with more numbers returns false', () => {
    expect(utils.validateCNPJ('12123123123222222222')).toBe(false);
  });
});