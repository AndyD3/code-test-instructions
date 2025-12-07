import { UseBaseMutationResult } from '@tanstack/react-query'

import { useMutation, useQueryClient } from '@tanstack/react-query'
import { ShortenUrlRequest } from '../types'
import { AxiosResponse } from 'axios'
import axiosClient from 'axios'

const createUrl = async (shortUrlRequest: ShortenUrlRequest): Promise<AxiosResponse<ShortenUrlRequest>> => {
  return await axiosClient.post<ShortenUrlRequest>('http://localhost:8080/shorten', shortUrlRequest)
}

export const useCreateUrl = (): UseBaseMutationResult<
  AxiosResponse<ShortenUrlRequest>,
  unknown,
  ShortenUrlRequest,
  unknown
> => {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (shortUrlRequest : ShortenUrlRequest ) => createUrl(shortUrlRequest),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['shortenedUrls'] })
    },
  })
}
