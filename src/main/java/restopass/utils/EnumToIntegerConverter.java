package restopass.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import restopass.dto.MembershipType;


@WritingConverter
public class EnumToIntegerConverter implements Converter<MembershipType, Integer> {

    @Override
    public Integer convert(MembershipType membershipType) {
        return membershipType.ordinal();
    }
}
